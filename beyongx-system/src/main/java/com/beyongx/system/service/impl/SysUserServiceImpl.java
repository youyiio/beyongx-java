package com.beyongx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.utils.PasswordEncoder;
import com.beyongx.common.utils.ValidateUtils;
import com.beyongx.common.vo.Result;
import com.beyongx.system.entity.SysJob;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysUser;
import com.beyongx.system.entity.SysUserRole;
import com.beyongx.system.entity.meta.UserMeta;
import com.beyongx.system.mapper.SysUserMapper;
import com.beyongx.system.mapper.SysUserRoleMapper;
import com.beyongx.system.service.ISysUserRoleService;
import com.beyongx.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beyongx.system.vo.SignUser;
import com.beyongx.system.vo.UserVo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public SysUser register(SignUser signUser, String ip) {
        String username = signUser.getUsername();

        String account = RandomStringUtils.randomAscii(20);
        String mobile = RandomStringUtils.randomAscii(11);
        String email = mobile + "@" + RandomStringUtils.randomAlphabetic(6).toLowerCase() + ".com";
        if (ValidateUtils.isValidEmail(username)) {
            email = username;
        } else if (ValidateUtils.isValidMobile(username)) {
            mobile = username;
        } else {
            account = username;
        }

        String nickname = StringUtils.isNotBlank(signUser.getNickname()) ? signUser.getNickname() : "用户" + mobile.substring(6);

        SysUser user = new SysUser();
        user.setAccount(account);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setNickname(nickname);

        user.setHeadUrl("/static/common/img/head/default.jpg");
        user.setStatus(UserMeta.Status.ACTIVE.getCode());

        Date registerTime = new Date();
        user.setRegisterTime(registerTime);
        user.setRegisterIp(ip);
        
        //密码加密SHA-256
        String salt = RandomStringUtils.randomAscii(32);
        String password = PasswordEncoder.encodePassword(signUser.getPassword(), salt);
        user.setPassword(password);
        user.setSalt(salt);

        baseMapper.insert(user);

        return user;
    }

    @Override
    public SysUser login(SignUser signUser, String ip) {
        String username = signUser.getUsername();
        String plainPassword = signUser.getPassword(); //明文密码
        
        SysUser user = null;
        if (ValidateUtils.isValidEmail(username)) {
            user = this.findByEmail(signUser.getUsername());
        } else if (ValidateUtils.isValidMobile(username)) {
            user = this.findByMobile(signUser.getUsername());
        } else {
            user = this.findByAccount(signUser.getUsername());
        }
        
        if (user == null) {
            throw new ServiceException(Result.Code.E_USER_NOT_EXIST, "用户不存在!");
        }
        if (!this.verifyPassword(plainPassword, user)) {
            throw new ServiceException(Result.Code.E_USER_PASSWORD_INCORRECT, "用户账号或密码不正确!");
        }

        
        if (user.getStatus() == UserMeta.Status.DELETED.getCode()) {
            throw new ServiceException(Result.Code.E_USER_NOT_EXIST, "用户不存在!");
        }
        if (user.getStatus() == UserMeta.Status.APPLY.getCode()) {
            throw new ServiceException(Result.Code.E_USER_STATUS_NOT_ACTIVED, "用户未激活!");
        }
        if (user.getStatus() == UserMeta.Status.FREED.getCode()) {
            throw new ServiceException(Result.Code.E_USER_STATUS_FREED, "用户已冻结!");
        }

        this.markLogin(user, ip);

        return user;
    }

    @Override
    public SysUser findByAccount(String account) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        SysUser user = baseMapper.selectOne(queryWrapper);

        return user;
    }

    @Override
    public SysUser findByEmail(String email) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        SysUser user = baseMapper.selectOne(queryWrapper);

        return user;
    }

    @Override
    public SysUser findByMobile(String mobile) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        SysUser user = baseMapper.selectOne(queryWrapper);

        return user;
    }

    @Override
    public boolean verifyPassword(String plainPassword, SysUser user) {

        //密码加密SHA-256
        String salt = user.getSalt();
        String password = PasswordEncoder.encodePassword(plainPassword, salt);

        return StringUtils.equals(password, user.getPassword());
    }

    @Override
    public List<SysRole> listRoles(Integer uid) {
        return baseMapper.selectRolesById(uid);
    }

    @Override
    public List<SysJob> listJobs(Integer uid) {
        return baseMapper.selectJobsById(uid);
    }

    private void markLogin(SysUser user, String ip) {
        user.setLastLoginIp(ip);
        user.setLastLoginTime(new Date());
        baseMapper.updateById(user);
    }

    @Override
    public SysUser createUser(UserVo userVo, String ip) {
        if (StringUtils.isBlank(userVo.getNickname())) {
            userVo.setNickname("用户" + userVo.getMobile().substring(6));
        }
        
        userVo.setEmail(userVo.getEmail().toLowerCase());
        if (this.findByEmail(userVo.getEmail()) != null) {
            throw new ServiceException(Result.Code.E_USER_EMAIL_HAS_EXIST, Result.Msg.E_USER_EMAIL_HAS_EXIST);
        }
        if (this.findByMobile(userVo.getMobile()) != null) {
            throw new ServiceException(Result.Code.E_USER_MOBILE_HAS_EXIST, Result.Msg.E_USER_MOBILE_HAS_EXIST);
        }
        if (StringUtils.isBlank(userVo.getAccount())) {
            userVo.setAccount(RandomStringUtils.randomAlphanumeric(24));
        }
        if (this.findByAccount(userVo.getAccount()) != null) {
            throw new ServiceException(Result.Code.E_USER_ACCOUNT_HAS_EXIST, Result.Msg.E_USER_ACCOUNT_HAS_EXIST);   
        }

        SysUser user = new SysUser();
        try {
            BeanUtils.copyProperties(user, userVo);
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        user.setHeadUrl("/static/common/img/head/default.jpg");
        user.setStatus(UserMeta.Status.ACTIVE.getCode());

        Date registerTime = new Date();
        user.setRegisterTime(registerTime);
        user.setRegisterIp(ip);
        
        //密码加密SHA-256
        String plainPassword = userVo.getPassword();

        String salt = RandomStringUtils.randomAscii(32);
        String password = PasswordEncoder.encodePassword(plainPassword, salt);
        user.setPassword(password);
        user.setSalt(salt);

        baseMapper.insert(user);

        return user;
    }

    @Override
    public SysUser editUser(UserVo userVo, String ip) {
        SysUser user = baseMapper.selectById(userVo.getId());
        if (user == null || user.getStatus() == UserMeta.Status.DELETED.getCode()) {
            throw new ServiceException(Result.Code.E_USER_NOT_EXIST, Result.Msg.E_USER_NOT_EXIST);
        }
        
        userVo.setEmail(userVo.getEmail().toLowerCase());
        if (!StringUtils.equals(user.getEmail(), userVo.getEmail())) {
            user.setEmail(userVo.getEmail());
            if (this.findByEmail(userVo.getEmail()) != null) {
                throw new ServiceException(Result.Code.E_USER_EMAIL_HAS_EXIST, "邮箱已经被其他用户占用!");
            }
        }
        if (!StringUtils.equals(user.getMobile(), userVo.getMobile())) {
            user.setMobile(userVo.getMobile());
            if (this.findByMobile(userVo.getMobile()) != null) {
                throw new ServiceException(Result.Code.E_USER_MOBILE_HAS_EXIST, "手机号已经被其他用户占用!");
            }
        }
        if (!StringUtils.equals(user.getAccount(), userVo.getAccount())) {
            user.setAccount(userVo.getAccount());
            if (this.findByAccount(userVo.getAccount()) != null) {
                throw new ServiceException(Result.Code.E_USER_MOBILE_HAS_EXIST, "用户名已经被其他用户占用!");
            }
        }        

        
        if (StringUtils.isNotBlank(userVo.getNickname())) {
            user.setNickname(userVo.getNickname());
        }
        if (StringUtils.isNotBlank(userVo.getQq())) {
            user.setQq(userVo.getQq());
        }
        if (StringUtils.isNotBlank(userVo.getWeixin())) {
            user.setWeixin(userVo.getWeixin());
        }

        baseMapper.updateById(user);

        return user;
    }

    //给用户分配
    @Override
    public List<SysRole> assignRoles(Integer uid, List<Integer> roleIds) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        userRoleMapper.delete(wrapper);

        List<SysUserRole> userRolelist = roleIds.stream().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setRoleId(roleId);
            userRole.setUid(uid);

            //userRoleMapper.insert(userRole);
            return userRole;
        }).collect(Collectors.toList());
        
        userRoleService.saveBatch(userRolelist);

        return this.listRoles(uid);
    }

    @Override
    public boolean modifyPassword(Integer uid, String plainPassword) {
        SysUser user = baseMapper.selectById(uid);
        if (user == null || user.getStatus() == UserMeta.Status.DELETED.getCode()) {
            throw new ServiceException(Result.Code.E_USER_NOT_EXIST, Result.Msg.E_USER_NOT_EXIST);
        }

        //密码加密SHA-256
        String salt = user.getSalt();
        String password = PasswordEncoder.encodePassword(plainPassword, salt);
        user.setPassword(password);

        baseMapper.updateById(user);

        return true;
    }

    @Override
    public boolean freeze(Integer uid) {
        SysUser user = baseMapper.selectById(uid);
        if (user == null || user.getStatus() == UserMeta.Status.DELETED.getCode()) {
            throw new ServiceException(Result.Code.E_USER_NOT_EXIST, Result.Msg.E_USER_NOT_EXIST);
        }

        if (user.getStatus() != UserMeta.Status.ACTIVE.getCode()) {
            throw new ServiceException(Result.Code.E_USER_STATUS_NOT_ACTIVED, "冻结失败：用户未激活!");
        }

        user.setStatus(UserMeta.Status.FREED.getCode());
        baseMapper.updateById(user);
        return true;
    }

    @Override
    public boolean unfreeze(Integer uid) {
        SysUser user = baseMapper.selectById(uid);
        if (user == null || user.getStatus() == UserMeta.Status.DELETED.getCode()) {
            throw new ServiceException(Result.Code.E_USER_NOT_EXIST, Result.Msg.E_USER_NOT_EXIST);
        }

        if (user.getStatus() != UserMeta.Status.FREED.getCode()) {
            throw new ServiceException(Result.Code.E_USER_STATUS_INVALID, "解冻失败：用户未被冻结!");
        }

        user.setStatus(UserMeta.Status.ACTIVE.getCode());
        baseMapper.updateById(user);
        return true;
    }

}

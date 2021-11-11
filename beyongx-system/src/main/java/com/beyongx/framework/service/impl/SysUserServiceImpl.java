package com.beyongx.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.utils.DateTimeUtils;
import com.beyongx.common.utils.PasswordEncoder;
import com.beyongx.common.utils.ValidateUtils;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.entity.SysRole;
import com.beyongx.framework.entity.SysUser;
import com.beyongx.framework.entity.meta.UserMeta;
import com.beyongx.framework.mapper.SysRoleMapper;
import com.beyongx.framework.mapper.SysUserMapper;
import com.beyongx.framework.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beyongx.framework.vo.SignUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    private SysRoleMapper roleMapper;

    @Override
    public SysUser register(SignUser signUser) {
        SysUser user = new SysUser();
        user.setAccount(signUser.getUsername());
        user.setEmail(signUser.getUsername());
        user.setMobile(signUser.getUsername());

        user.setStatus(1);

        Date registerTime = new Date();
        user.setRegisterTime(new Date());

        //密码加密SHA-256
        String salt = DateTimeUtils.getFormatString(registerTime, "yyyy-MM-dd HH:mm:ss");
        String password = PasswordEncoder.encodePassword(signUser.getPassword(), salt);
        user.setPassword(password);

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
            throw new ServiceException(Result.Code.E_USER_STATE_NOT_ACTIVED, "用户未激活!");
        }
        if (user.getStatus() == UserMeta.Status.FREED.getCode()) {
            throw new ServiceException(Result.Code.E_USER_STATE_FREED, "用户已冻结!");
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
        return roleMapper.selectByUid(uid);
    }

    private void markLogin(SysUser user, String ip) {
        user.setLastLoginIp(ip);
        user.setLastLoginTime(new Date());
        baseMapper.updateById(user);
    }
}

package com.beyongx.system.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.utils.IpUtils;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.validation.group.Create;
import com.beyongx.common.validation.group.Edit;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.SysDept;
import com.beyongx.system.entity.SysJob;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysUser;
import com.beyongx.system.entity.meta.UserMeta;
import com.beyongx.system.service.ISysDeptService;
import com.beyongx.system.service.ISysRoleService;
import com.beyongx.system.service.ISysUserService;
import com.beyongx.system.vo.UserVo;
import com.beyongx.system.vo.UserVo.ModifyPassword;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户类 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysRoleService roleService;

    @RequiresPermissions("user:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        //排除content字段
        queryWrapper.select(SysUser.class, entity -> !entity.getColumn().equals("password"));

        //过滤条件
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        if (filters.containsKey("status")) {
            queryWrapper.eq("status", filters.remove("status"));
        } else {
            queryWrapper.ne("status", -1);
        }
        if (filters.containsKey("id")) {
            queryWrapper.eq("id", filters.remove("id"));
        }

        //其他条件
        for (String key : filters.keySet()) {
            queryWrapper.like(key, "%" + filters.get(key) + "%");
        }

        //排序
        Map<String, String> orders = pageVo.getOrders();
        if (orders.size() == 0) {
            queryWrapper.orderByDesc("id");
        } else {
            for (String key : orders.keySet()) {
                String val = orders.get(key);
                Boolean isAsc = val.equalsIgnoreCase("asc");
                queryWrapper.orderBy(true, isAsc, key);
            }            
        }

        IPage<SysUser> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        
        IPage<SysUser> pageList = userService.page(page, queryWrapper);
        if (CollectionUtils.isEmpty(pageList.getRecords())) {
            return Result.success(pageList);
        }

        IPage<UserVo> pageUserVoList = new Page<>(pageVo.getPage(), pageVo.getSize());
        pageUserVoList.setTotal(page.getTotal());

        List<UserVo> list = new ArrayList<>();
        for (SysUser user : pageList.getRecords()) {
            UserVo userVo = new UserVo();
            try {
                BeanUtils.copyProperties(userVo, user);
            } catch (Exception e) {
                log.error("beanutils copy properties error", e);
            }

            SysDept dept = deptService.getById(userVo.getDeptId());
            userVo.setDept(dept);

            List<SysRole> roles = userService.listRoles(userVo.getId());
            userVo.setRoles(roles);
            List<Integer> roleIds = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
            userVo.setRoleIds(roleIds);

            List<SysJob> jobs = userService.listJobs(userVo.getId());
            userVo.setJobs(jobs);
            List<Integer> jobIds = jobs.stream().map(job -> job.getId()).collect(Collectors.toList());
            userVo.setJobIds(jobIds);

            list.add(userVo);
        }
        pageUserVoList.setRecords(list);

        return Result.success(pageUserVoList);
    }

    @RequiresPermissions("user:query")
    @GetMapping("/{id}")
    public Result query(@PathVariable(value="id") Integer id) {
        SysUser user = userService.getById(id);
        if (user == null) {
            return Result.error(Result.Code.E_USER_NOT_EXIST, "用户不存在!");
        }

        user.setPassword(null);
        user.setSalt(null);

        UserVo userVo = new UserVo();
        try {
            BeanUtils.copyProperties(userVo, user);
        } catch (Exception e) {
            log.error("beanutils copy properties error", e);
        }

        SysDept dept = deptService.getById(userVo.getDeptId());
        userVo.setDept(dept);

        List<SysRole> roles = userService.listRoles(userVo.getId());
        userVo.setRoles(roles);
        List<Integer> roleIds = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
        userVo.setRoleIds(roleIds);

        List<SysJob> jobs = userService.listJobs(userVo.getId());
        userVo.setJobs(jobs);
        List<Integer> jobIds = jobs.stream().map(job -> job.getId()).collect(Collectors.toList());
        userVo.setJobIds(jobIds);

        return Result.success(userVo);
    }

    @RequiresPermissions("user:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class, Create.class}) @RequestBody UserVo userVo, HttpServletRequest request) {

        String ip = IpUtils.getIpAddr(request);

        SysUser user = userService.createUser(userVo, ip);
        if (user == null) {
            return Result.error(Result.Code.E_DB_ERROR, "用户创建失败!");
        }

        List<SysRole> roles = null;
        if (CollectionUtils.isNotEmpty(userVo.getRoleIds())) {
            roles = userService.assignRoles(user.getId(), userVo.getRoleIds());
        } else {
            roles = userService.listRoles(userVo.getId());
        }

        UserVo newUserVo = new UserVo();
        try {
            BeanUtils.copyProperties(newUserVo, user);
        } catch (Exception e) {
            log.error("beanutils copy properties error", e);
        }

        newUserVo.setPassword(null);
        
        SysDept dept = deptService.getById(newUserVo.getDeptId());
        newUserVo.setDept(dept);

        newUserVo.setRoles(roles);
        List<Integer> roleIds = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
        userVo.setRoleIds(roleIds);

        return Result.success(newUserVo);
    }

    @RequiresPermissions("user:edit")
    @PostMapping("/edit")
    public Result edit(@Validated({Always.class, Edit.class}) @RequestBody UserVo userVo, HttpServletRequest request) {
        String ip = IpUtils.getIpAddr(request);

        SysUser user = userService.editUser(userVo, ip);
        if (user == null) {
            return Result.error(Result.Code.E_DB_ERROR, "用户创建失败!");
        }

        List<SysRole> roles = null;
        if (CollectionUtils.isNotEmpty(userVo.getRoleIds())) {
            roles = userService.assignRoles(user.getId(), userVo.getRoleIds());
        } else {
            roles = userService.listRoles(userVo.getId());
        }

        UserVo newUserVo = new UserVo();
        try {
            BeanUtils.copyProperties(newUserVo, user);
        } catch (Exception e) {
            log.error("beanutils copy properties error", e);
        }

        newUserVo.setPassword(null);
        
        SysDept dept = deptService.getById(newUserVo.getDeptId());
        newUserVo.setDept(dept);

        newUserVo.setRoles(roles);
        List<Integer> roleIds = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
        userVo.setRoleIds(roleIds);

        //设置岗位
        List<SysJob> jobs = null;
        if (CollectionUtils.isNotEmpty(userVo.getJobIds())) {
            jobs = userService.assignJobs(user.getId(), userVo.getJobIds());
        } else {
            jobs = userService.listJobs(user.getId());
        }
        newUserVo.setJobs(jobs);
        List<Integer> jobIds = jobs.stream().map(job -> job.getId()).collect(Collectors.toList());
        userVo.setJobIds(jobIds);

        return Result.success(newUserVo);
    }

    @RequiresPermissions("user:delete")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        SysUser user = userService.getById(id);
        if (user == null || user.getStatus() == UserMeta.Status.DELETED.getCode()) {
            return Result.error(Result.Code.E_USER_NOT_EXIST, Result.Msg.E_USER_NOT_EXIST);
        }

        user.setStatus(UserMeta.Status.DELETED.getCode());
        userService.updateById(user);

        return Result.success(null);
    }

    @RequiresPermissions("user:modifyPassword")
    @PostMapping("/modifyPassword")
    public Result modifyPassword(@Validated({Always.class, ModifyPassword.class}) @RequestBody UserVo userVo) {
        SysUser user = userService.getById(userVo.getId());
        if (user == null || user.getStatus() == UserMeta.Status.DELETED.getCode()) {
            return Result.error(Result.Code.E_USER_NOT_EXIST, Result.Msg.E_USER_NOT_EXIST);
        }

        userService.modifyPassword(userVo.getId(), userVo.getPassword());

        return Result.success(null);
    }

    @RequiresPermissions("user:freeze")
    @PostMapping("/freeze")
    public Result freeze(@RequestBody UserVo userVo) {
        Integer uid = userVo.getId();
        if (uid == null) {
            return Result.error(Result.Code.E_PARAM_ERROR, "用户ID不能为空");
        }

        userService.freeze(uid);

        return Result.success(null);
    }

    @RequiresPermissions("user:unfreeze")
    @PostMapping("/unfreeze")
    public Result unfreeze(@RequestBody UserVo userVo) {
        Integer uid = userVo.getId();
        if (uid == null) {
            return Result.error(Result.Code.E_PARAM_ERROR, "用户ID不能为空");
        }

        userService.unfreeze(uid);

        return Result.success(null);
    }

    @RequiresPermissions("user:addRoles")
    @PostMapping("/addRoles")
    public Result addRoles(@RequestBody UserVo userVo) {
        if (userVo.getRoleIds() == null || userVo.getRoleIds().size() == 0) {
            return Result.error(Result.Code.E_PARAM_VALIDATE_ERROR, "参数roleIds不能为空!");
        }

        SysUser user = userService.getById(userVo.getId());
        if (user == null || user.getStatus() == UserMeta.Status.DELETED.getCode()) {
            return Result.error(Result.Code.E_USER_NOT_EXIST, Result.Msg.E_USER_NOT_EXIST);
        }

        List<SysRole> roles = userService.assignRoles(user.getId(), userVo.getRoleIds());

        UserVo newUserVo = new UserVo();
        try {
            BeanUtils.copyProperties(newUserVo, user);
        } catch (Exception e) {
            log.error("beanutils copy properties error", e);
        }

        newUserVo.setPassword(null);
        
        SysDept dept = deptService.getById(newUserVo.getDeptId());
        newUserVo.setDept(dept);

        newUserVo.setRoles(roles);

        return Result.success(newUserVo);
    }

    @RequiresPermissions("user:quickSelect")
    @RequestMapping(value="/quickSelect", method = {RequestMethod.GET, RequestMethod.POST})
    public Result quickSelect(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        //排除content字段
        queryWrapper.select("id", "account", "nickname", "sex", "mobile", "email");

        //过滤条件
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        
        Integer id = (Integer)filters.remove("id");
        if (id != null) {
            queryWrapper.eq("id", id);
        }

        String nickname = (String)filters.remove("nickname");
        if (StringUtils.isNotBlank(nickname)) {
            queryWrapper.like("nickname", "%" + nickname + "%");
        }

        String account = (String)filters.remove("account");
        if (StringUtils.isNotBlank(account)) {
            queryWrapper.like("account", "%" + account + "%");
        }

        String mobile = (String)filters.remove("mobile");
        if (StringUtils.isNotBlank(mobile)) {
            queryWrapper.like("mobile", "%" + mobile + "%");
        }

        String email = (String)filters.remove("email");
        if (StringUtils.isNotBlank(email)) {
            queryWrapper.like("nickname", "%" + email + "%");
        }

        //排序
        Map<String, String> orders = pageVo.getOrders();
        if (orders.size() == 0) {
            queryWrapper.orderByDesc("id");
        } else {
            for (String key : orders.keySet()) {
                String val = orders.get(key);
                Boolean isAsc = val.equalsIgnoreCase("asc");
                queryWrapper.orderBy(true, isAsc, key);
            }            
        }

        IPage<Map<String, Object>> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        
        IPage<Map<String, Object>> pageList = userService.pageMaps(page, queryWrapper);

        return Result.success(pageList);
    }
}

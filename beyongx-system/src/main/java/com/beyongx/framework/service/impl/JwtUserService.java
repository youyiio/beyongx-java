package com.beyongx.framework.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.beyongx.framework.service.IJwtUserService;
import com.beyongx.framework.shiro.JwtPermission;
import com.beyongx.framework.shiro.JwtRole;
import com.beyongx.framework.shiro.JwtUser;
import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysUser;
import com.beyongx.system.service.ISysRoleService;
import com.beyongx.system.service.ISysUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class JwtUserService implements IJwtUserService {

    @Autowired
    @Lazy
    private ISysUserService userService;

    @Autowired
    @Lazy
    private ISysRoleService roleService;
    
    @Override
    @Cacheable(cacheNames = "jwtuser", key = "#uid")
    public JwtUser getByUId(Integer uid) {
        SysUser user = userService.getById(uid);

        JwtUser jwtUser = null;
        // SysUser user = (SysUser)redisUtils.get("user:" + uid);
        if (user != null) {
            //user.setPassword(null);
            //user.setSalt(null);
            //redisUtils.set("user:" + uid, user, 60 * 60L, TimeUnit.SECONDS);

            jwtUser = new JwtUser();
            jwtUser.setUid(user.getId());
            //jwtUser.setUsername(username);

            List<JwtRole> jwtRoleList = this.findByUid(uid);
            jwtUser.setRoleList(jwtRoleList);            
        }
        
        return jwtUser;
    }

    @Override
    public List<JwtRole> findByUid(Integer uid) {
        List<SysRole> roleList = userService.listRoles(uid);

        List<JwtRole> jwtRoleList = new ArrayList<>();
        for (SysRole role : roleList) {
            List<JwtPermission> permissionList = this.findByRoleId(role.getId());
            JwtRole jwtRole = new JwtRole(role.getId(), role.getName(), permissionList);
            jwtRoleList.add(jwtRole);
        }

        return jwtRoleList;
    }

    @Override
    public List<JwtPermission> findByRoleId(Integer roleId) {
        List<SysMenu> menuList = roleService.listMenu(roleId);

        List<JwtPermission> permissionList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            JwtPermission permission = new JwtPermission(menu.getId(), menu.getPermission(), menu.getPath());
            permissionList.add(permission);
        }

        return permissionList;
    }
    
}

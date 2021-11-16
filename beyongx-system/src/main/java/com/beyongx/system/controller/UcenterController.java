package com.beyongx.system.controller;


import com.beyongx.common.utils.TreeUtils;
import com.beyongx.common.vo.Result;
import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysUser;
import com.beyongx.system.entity.SysUserMeta;
import com.beyongx.system.service.ISysMenuService;
import com.beyongx.system.service.ISysUserMetaService;
import com.beyongx.system.service.ISysUserService;
import com.beyongx.system.vo.MenuVo;
import com.beyongx.system.vo.UcenterVo;
import com.beyongx.framework.shiro.JwtUser;
import com.beyongx.framework.shiro.JwtUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户中心接口类 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/ucenter")
@Slf4j
public class UcenterController {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysUserMetaService userMetaService;
    @Autowired
    private ISysMenuService menuService;

    @RequiresPermissions("ucenter:getInfo")
    @GetMapping("/getInfo")
    public Result getInfo() {
        JwtUser jwtUser = JwtUtils.getUser();

        SysUser user = userService.getById(jwtUser.getUid());

        Map<String, Object> data = new HashMap<>();
        data.put("uid", user.getId());
        data.put("nickname", user.getNickname());
        data.put("headUrl", user.getHeadUrl());
        data.put("description", "");
        
        SysUserMeta userMeta = userMetaService.meta(user.getId(), "description");
        if (userMeta != null) {
            data.put("description", userMeta.getMetaValue());
        }

        List<SysRole> roleList = userService.listRoles(user.getId());
        List<String> roles = new ArrayList<>();
        for (SysRole role : roleList) {
            roles.add(role.getName());
        }
        
        data.put("roles", roles);

        return Result.success(data);
    }

    @RequiresPermissions("ucenter:profile")
    @PostMapping("/profile")
    public Result profile(@RequestBody UcenterVo ucenterVo) {
        JwtUser jwtUser = JwtUtils.getUser();

        SysUser user = userService.getById(jwtUser.getUid());
        user.setNickname(ucenterVo.getNickname());
        user.setQq(ucenterVo.getQq());
        user.setWeixin(ucenterVo.getWeixin());

        if (StringUtils.isNotEmpty(ucenterVo.getDescription())) {
            userMetaService.meta(user.getId(), "description", ucenterVo.getDescription());
        }

        userService.updateById(user);

        Map<String, Object> data = new HashMap<>();
        data.put("uid", user.getId());
        data.put("nickname", user.getNickname());
        data.put("headUrl", user.getHeadUrl());
        data.put("description", ucenterVo.getDescription());
        data.put("qq", user.getQq());

        return Result.success(data);
    }

    @RequiresPermissions("ucenter:profile")
    @GetMapping("/menus")
    public Result menus() {
        JwtUser jwtUser = JwtUtils.getUser();

        List<SysRole> roleList = userService.listRoles(jwtUser.getUid());
        List<Integer> roleIdList = new ArrayList<>();
        for (SysRole role : roleList) {
            roleIdList.add(role.getId());
        }

        Integer[] roleIds = new Integer[roleIdList.size()];
        List<SysMenu> menuList = menuService.listMenusByRoleIdsAndOther(roleIdList.toArray(roleIds), true);
        
        List<MenuVo> menuVoList = menuList.stream().map(menu -> {
            MenuVo menuVo = new MenuVo();
            try {
                BeanUtils.copyProperties(menuVo, menu);
            } catch(Exception e) {
                log.error("bean copy error", e);
            }
            
            return menuVo;
        }).collect(Collectors.toList());

        List<MenuVo> treeMenuVoList = TreeUtils.parse(menuVoList);

        return Result.success(treeMenuVoList);
    }
}

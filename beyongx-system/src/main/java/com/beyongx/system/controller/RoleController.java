package com.beyongx.system.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.validation.group.Create;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.SysDept;
import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysUser;
import com.beyongx.system.entity.meta.RoleMeta;
import com.beyongx.system.service.ISysDeptService;
import com.beyongx.system.service.ISysRoleService;
import com.beyongx.system.service.ISysUserService;
import com.beyongx.system.vo.RoleVo;
import com.beyongx.system.vo.UserVo;

import org.apache.commons.collections.CollectionUtils;
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
 * 角色表 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/role")
@Slf4j
public class RoleController {
    
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService deptService;

    @RequiresPermissions("role:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        
        if (filters.containsKey("keyword")) {
            String keyword = (String)filters.remove("keyword");
            queryWrapper.like("title", keyword);
        }
        queryWrapper.ne("status", RoleMeta.Status.DELETED.getCode());
        
        //排序
        Map<String, String> orders = pageVo.getOrders();
        if (orders.size() == 0) {
            queryWrapper.orderByAsc("id");
        } else {
            for (String key : orders.keySet()) {
                String val = orders.get(key);
                Boolean isAsc = val.equalsIgnoreCase("asc");
                queryWrapper.orderBy(true, isAsc, key);
            }
        }

        IPage<SysRole> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        
        IPage<SysRole> pageList = roleService.page(page, queryWrapper);
        if (CollectionUtils.isEmpty(pageList.getRecords())) {
            return Result.success(pageList);
        }

        //封装结果数据
        List<Map<String, Object>> maps = BeanUtils.beansToMaps(pageList.getRecords());
        List<RoleVo> list = BeanUtils.mapsToBeans(maps, RoleVo.class);
        for (RoleVo vo : list) {
            List<Integer> menuIds = roleService.listMenuId(vo.getId(), "api");
            vo.setMenuIds(menuIds);
        };

        IPage<RoleVo> newPageList = new Page<>(page.getCurrent(), page.getSize());
        newPageList.setPages(page.getPages());
        newPageList.setTotal(page.getTotal());
        newPageList.setRecords(list);

        return Result.success(newPageList);
    }

    @RequiresPermissions("role:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class, Create.class}) @RequestBody RoleVo roleVo) {
        SysRole role = roleService.createRole(roleVo);

        return Result.success(role);
    }

    @RequiresPermissions("role:edit")
    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody RoleVo roleVo) {
        SysRole role = roleService.editRole(roleVo);

        return Result.success(role);
    }

    @RequiresPermissions("role:delete")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        SysRole role = roleService.getById(id);
        if (role == null || role.getStatus() == RoleMeta.Status.DELETED.getCode()) {
            return Result.error(Result.Code.E_ROLE_NOT_FOUND, Result.Msg.E_ROLE_NOT_FOUND);
        }

        role.setStatus(RoleMeta.Status.DELETED.getCode());
        roleService.updateById(role);

        return Result.success(null);
    }

    @RequiresPermissions("role:menus")
    @GetMapping("/menus/{id}")
    public Result listMenu(@PathVariable(value="id") Integer id) {
        List<SysMenu> menus = roleService.listMenu(id, "api");

        return Result.success(menus);
    }

    @RequiresPermissions("role:addMenus")
    @PostMapping("/addMenus/{id}")
    public Result addMenus(@PathVariable(value="id") Integer id, @RequestBody RoleVo roleVo) {
        List<Integer> menus = roleVo.getMenuIds();
        if (CollectionUtils.isEmpty(menus)) {
            return Result.error(Result.Code.E_PARAM_VALIDATE_ERROR, "参数menuIds不能为空");
        }

        boolean success = roleService.addMenus(id, roleVo.getMenuIds());
        if (!success) {
            return Result.error(Result.Code.E_UNKNOWN_ERROR, "分配菜单失败!");
        }

        return Result.success(null);
    }

    @RequiresPermissions("role:users")
    @GetMapping("/users/{id}")
    public Result listUser(@PathVariable(value="id") Integer id, @Validated @RequestBody PageVo pageVo) {

        IPage<SysUser> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        IPage<SysUser> pageList = roleService.listUser(page, id);

        IPage<UserVo> pageUserVoList = new Page<>(pageVo.getPage(), pageVo.getSize());
        pageUserVoList.setTotal(page.getTotal());

        List<UserVo> list = new ArrayList<>();
        for (SysUser user : pageList.getRecords()) {
            UserVo userVo = new UserVo();
            try {
                org.apache.commons.beanutils.BeanUtils.copyProperties(userVo, user);
            } catch (Exception e) {
                log.error("beanutils copy properties error", e);
            }

            SysDept dept = deptService.getById(userVo.getDeptId());
            userVo.setDept(dept);

            List<SysRole> roles = userService.listRoles(userVo.getId());
            userVo.setRoles(roles);

            list.add(userVo);
        }
        pageUserVoList.setRecords(list);
        
        return Result.success(pageUserVoList);
    }
}

package com.beyongx.system.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.utils.TreeUtils;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.validation.group.Create;
import com.beyongx.common.validation.group.Edit;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.utils.PageUtils;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.entity.meta.MenuMeta;
import com.beyongx.system.service.ISysMenuService;
import com.beyongx.system.vo.MenuVo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/menu")
@Slf4j
public class MenuController {
    
    @Autowired
    private ISysMenuService menuService;
    
    @RequiresPermissions("menu:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        
        queryWrapper.eq("status", MenuMeta.Status.ACTIVE.getCode());
        queryWrapper.eq("belongs_to", "api");

        if (filters.containsKey("keyword")) {
            String keyword = (String)filters.remove("keyword");
            queryWrapper.like("title", keyword);
        }
        Integer pid = 0;
        if (filters.containsKey("pid")) {
            pid = (Integer)filters.remove("pid");
        }
        String struct = "tree";
        if (filters.containsKey("struct")) {
            struct = (String)filters.remove("struct");
        }
        Integer depth = 0; // 0表示没有限制
        if (filters.containsKey("depth")) {
            depth = (Integer)filters.remove("depth");
        }

        //排序
        Map<String, String> orders = pageVo.getOrders();
        if (orders.size() == 0) {
            queryWrapper.orderByAsc("pid").orderByAsc("sort");
        } else {
            for (String key : orders.keySet()) {
                String val = orders.get(key);
                Boolean isAsc = val.equalsIgnoreCase("asc");
                queryWrapper.orderBy(true, isAsc, key);
            }
        }

        List<SysMenu> menuList = menuService.list(queryWrapper);
        if (CollectionUtils.isEmpty(menuList)) {
            IPage<MenuVo> pageList = new Page<>(pageVo.getPage(), pageVo.getSize());
            return Result.success(pageList);
        }

        List<MenuVo> menuVoList = menuList.stream().map(menu -> {
            MenuVo menuVo = new MenuVo();
            try {
                BeanUtils.copyProperties(menuVo, menu);
            } catch(Exception e) {
                log.error("bean copy error", e);
            }
            
            return menuVo;
        }).collect(Collectors.toList());

        List<MenuVo> treeMenuVoList = TreeUtils.parse(pid, menuVoList);

        IPage<MenuVo> pageList = PageUtils.getPages(pageVo.getPage(), pageVo.getSize(), treeMenuVoList);
        
        return Result.success(pageList);
    }

    @RequiresPermissions("menu:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class, Create.class}) @RequestBody MenuVo menuVo) {
        SysMenu menu = menuService.createMenu(menuVo);

        return Result.success(menu);
    }

    @RequiresPermissions("menu:edit")
    @PostMapping("/edit")
    public Result edit(@Validated({Always.class, Edit.class}) @RequestBody MenuVo menuVo) {
        SysMenu menu = menuService.editMenu(menuVo);

        return Result.success(menu);
    }

    @RequiresPermissions("menu:delete")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        SysMenu menu = menuService.getById(id);
        if (menu == null) {
            return Result.error(Result.Code.E_MENU_NOT_FOUND, "菜单不存在!");
        }

        menuService.removeById(id);

        return Result.success(null);
    }

}

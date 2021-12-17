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
import com.beyongx.system.entity.CmsLink;
import com.beyongx.system.entity.meta.MenuMeta;
import com.beyongx.system.service.ICmsLinkService;
import com.beyongx.system.vo.LinkVo;

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

@RestController
@RequestMapping("/api/link")
@Slf4j
public class LinkController {
    @Autowired
    private ICmsLinkService linkService;

    @RequiresPermissions("link:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<CmsLink> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        
        if (filters.containsKey("status")) {
            Integer status = (Integer)filters.remove("status");
            queryWrapper.eq("status", status);
        }

        if (filters.containsKey("keyword")) {
            String keyword = (String)filters.remove("keyword");
            queryWrapper.like("title", keyword);
        }
        

        //排序
        Map<String, String> orders = pageVo.getOrders();
        if (orders.size() == 0) {
            queryWrapper.orderByDesc("sort");
        } else {
            for (String key : orders.keySet()) {
                String val = orders.get(key);
                Boolean isAsc = val.equalsIgnoreCase("asc");
                queryWrapper.orderBy(true, isAsc, key);
            }
        }

        IPage<CmsLink> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        IPage<CmsLink> pageList = linkService.page(page, queryWrapper);
        if (CollectionUtils.isEmpty(pageList.getRecords())) {
            return Result.success(pageList);
        }
        
        return Result.success(pageList);
    }

    @RequiresPermissions("link:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class, Create.class}) @RequestBody LinkVo linkVo) {
        CmsLink menu = linkService.createLink(linkVo);

        return Result.success(menu);
    }

    @RequiresPermissions("link:edit")
    @PostMapping("/edit")
    public Result edit(@Validated({Always.class, Edit.class}) @RequestBody LinkVo linkVo) {
        CmsLink menu = linkService.editLink(linkVo);

        return Result.success(menu);
    }

    @RequiresPermissions("link:delete")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        CmsLink menu = linkService.getById(id);
        if (menu == null) {
            return Result.error(Result.Code.E_MENU_NOT_FOUND, "友链不存在!");
        }

        linkService.removeById(id);

        return Result.success(null);
    }
}

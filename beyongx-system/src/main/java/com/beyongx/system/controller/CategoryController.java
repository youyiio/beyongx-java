package com.beyongx.system.controller;


import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.utils.TreeUtils;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.utils.PageUtils;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.CmsCategory;
import com.beyongx.system.entity.meta.CategoryMeta;
import com.beyongx.system.service.ICmsCategoryService;
import com.beyongx.system.vo.CategoryVo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
 * 分类表 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/category")
@Slf4j
public class CategoryController {

    @Autowired
    private ICmsCategoryService categoryService;
    
    @RequiresPermissions("category:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<CmsCategory> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());

        IPage<CmsCategory> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        
        IPage<CmsCategory> pageList = categoryService.page(page, queryWrapper);
        if (CollectionUtils.isEmpty(pageList.getRecords())) {
            return Result.success(pageList);
        }

        List<CategoryVo> categoryList = pageList.getRecords().stream().map(record -> {
            CategoryVo vo = new CategoryVo();
            try {
                BeanUtils.copyProperties(vo, record);
            } catch(Exception e) {
                log.error("bean copy error", e);
            }
            
            return vo;
        }).collect(Collectors.toList());
        
        //过滤树型结构集合
        Integer pid = 0;
        if (filters.containsKey("pid")) {
            pid = (Integer)filters.remove("pid");
        }
        String struct = "tree";
        if (filters.containsKey("struct")) {
            struct = (String)filters.remove("struct");
        }
        Integer depth = 3;
        if (filters.containsKey("depth")) {
            depth = (Integer)filters.remove("depth");
        }

        List<CategoryVo> parentList = TreeUtils.parse(pid, categoryList);
        
        IPage<CategoryVo> newPageList = PageUtils.getPages((int)page.getCurrent(), (int)page.getSize(), parentList);

        return Result.success(newPageList);
    }

    @RequiresPermissions("category:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class}) @RequestBody CategoryVo categoryVo) {
        CmsCategory category = new CmsCategory();
        try {
            BeanUtils.copyProperties(category, categoryVo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        if (category.getPid() == null) {
            category.setPid(0);
        }

        category.setStatus(CategoryMeta.Status.ONLINE.getCode());
        category.setCreateTime(new Date());

        boolean success = categoryService.save(category);
        if (!success) {
            throw new ServiceException(Result.Code.ACTION_FAILED, "新建分类失败!");
        }

        return Result.success(category);
    }

    @RequiresPermissions("category:edit")
    @PostMapping("/edit")
    public Result edit(@Validated({Always.class}) @RequestBody CategoryVo categoryVo) {
        if (categoryVo.getId() == null || StringUtils.isBlank(categoryVo.getId().toString())) {
            return Result.error(Result.Code.E_DATA_VALIDATE_ERROR, "分类ID不能为空!");
        }

        CmsCategory category = new CmsCategory();
        try {
            BeanUtils.copyProperties(category, categoryVo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        boolean success = categoryService.updateById(category);
        if (!success) {
            return Result.error(Result.Code.ACTION_FAILED, "更新分类失败!");
        }

        category = categoryService.getById(category.getId());

        return Result.success(category);
    }

    @RequiresPermissions("category:setStatus")
    @PostMapping("/setStatus")
    public Result setStatus(@RequestBody CategoryVo categoryVo) {
        if (categoryVo.getId() == null) {
            return Result.error(Result.Code.E_DATA_VALIDATE_ERROR, "分类id不能为空!"); 
        }
        if (categoryVo.getStatus() == null || 
           (categoryVo.getStatus() != CategoryMeta.Status.ONLINE.getCode() && categoryVo.getStatus() != CategoryMeta.Status.OFFLINE.getCode())) {
            return Result.error(Result.Code.E_DATA_VALIDATE_ERROR, "分类status值不合法!"); 
        }
        
        CmsCategory category = categoryService.getById((Integer)categoryVo.getId());
        if (category == null) {
            return Result.error(Result.Code.E_DATA_NOT_FOUND, "分类不存在!");
        }

        category.setStatus(categoryVo.getStatus());
        categoryService.updateById(category);

        return Result.success(null);
    }

    @RequiresPermissions("category:delete")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        CmsCategory category = categoryService.getById(id);
        if (category == null) {
            return Result.error(Result.Code.E_DATA_NOT_FOUND, "分类不存在!");
        }

        categoryService.removeById(id);

        return Result.success(null);
    }

}

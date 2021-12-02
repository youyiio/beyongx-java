package com.beyongx.system.controller;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.shiro.JwtUser;
import com.beyongx.framework.shiro.JwtUtils;
import com.beyongx.framework.vo.BatchIdVo;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.CmsArticle;
import com.beyongx.system.entity.CmsCategory;
import com.beyongx.system.entity.SysFile;
import com.beyongx.system.entity.meta.ArticleMeta;
import com.beyongx.system.service.ICmsArticleService;
import com.beyongx.system.service.ISysFileService;
import com.beyongx.system.vo.ArticleVo;
import com.beyongx.system.vo.AuditBatchIdVo;

import org.apache.commons.beanutils.PropertyUtils;
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
 * 文章表 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/article")
@Slf4j
public class ArticleController {

    @Autowired
    private ICmsArticleService articleService;
    @Autowired
    private ISysFileService fileService;

    @RequiresPermissions("article:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<CmsArticle> queryWrapper = new QueryWrapper<>();
        //排除content字段
        queryWrapper.select(CmsArticle.class, entity -> !entity.getColumn().equals("content"));
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        
        if (filters.containsKey("keyword")) {
            String keyword = (String)filters.remove("keyword");
            queryWrapper.like("title", keyword);
        }
        
        if (filters.containsKey("status")) {
            queryWrapper.eq("status", filters.remove("status"));
        } else {
            queryWrapper.ne("status", ArticleMeta.Status.DELETED.getCode());
        }

        if (filters.containsKey("startTime")) {
            String startTime = (String)filters.remove("startTime");
            queryWrapper.ge("create_time", startTime);
        }
        if (filters.containsKey("endTime")) {
            String endTime = (String)filters.remove("endTime");
            queryWrapper.lt("create_time", endTime);
        }
        //其他条件
        for (String key : filters.keySet()) {
            queryWrapper.eq(key, filters.get(key));
        }

        //排序
        Map<String, String> orders = pageVo.getOrders();
        if (orders.size() == 0) {
            queryWrapper.orderByDesc("sort").orderByDesc("post_time");
        } else {
            for (String key : orders.keySet()) {
                String val = orders.get(key);
                Boolean isAsc = val.equalsIgnoreCase("asc");
                queryWrapper.orderBy(true, isAsc, key);
            }            
        }

        IPage<CmsArticle> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        IPage<CmsArticle> pageList = new Page<>(pageVo.getPage(), pageVo.getSize());
        if (pageVo.getFilters().containsKey("categoryId")) {
            queryWrapper.eq("category_id", filters.get("categoryId"));

            pageList = articleService.listByCategoryId(page, (Integer)filters.get("categoryId"));
            if (CollectionUtils.isEmpty(pageList.getRecords())) {
                return Result.success(pageList);
            }
        } else {
            pageList = articleService.page(page, queryWrapper);
            if (CollectionUtils.isEmpty(pageList.getRecords())) {
                return Result.success(pageList);
            }
        }
        

        IPage<Map<String, Object>> resultList = new Page<>(pageVo.getPage(), pageVo.getSize());
        List<Map<String, Object>> mapList = new ArrayList<>(pageVo.getSize());
        pageList.getRecords().forEach(record -> {            
            Map<String, Object> vo = null;
            try {
                vo = PropertyUtils.describe(record);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(e.getMessage(), e);
                return;
            }

            List<CmsCategory> categorys = articleService.listCategorys(record.getId());
            vo.put("categorys", categorys);

            Integer thumbImageId = record.getThumbImageId();
            SysFile thumbImage = fileService.getById(thumbImageId);
            vo.put("thumbImage", thumbImage);

            List<SysFile> metaImages = new ArrayList<>();
            vo.put("metaImages", metaImages);

            List<SysFile> metaFiles = new ArrayList<>();
            vo.put("metaFiles", metaFiles);

            mapList.add(vo);
        });
        
        resultList.setRecords(mapList);
        resultList.setPages(pageList.getPages());
        resultList.setTotal(pageList.getTotal());

        return Result.success(resultList);
    }

    @RequiresPermissions("article:query")
    @GetMapping("/{id}")
    public Result query(@PathVariable(value="id") Integer id) {

        ArticleVo articleVo = articleService.getArticle(id);
        
        return Result.success(articleVo);
    }

    @RequiresPermissions("article:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class, ArticleVo.Create.class}) @RequestBody ArticleVo articleVo) {
        JwtUser jwtUser = JwtUtils.getUser();
        articleVo.setUid(jwtUser.getUid());

        ArticleVo resultVo = articleService.createArticle(articleVo);

        return Result.success(resultVo);
    }

    @RequiresPermissions("article:edit")
    @PostMapping("/{id}")
    public Result edit(@Validated({Always.class, ArticleVo.Edit.class}) @RequestBody ArticleVo articleVo, @PathVariable(value="id") Integer id) {
        if (id != articleVo.getId()) {
            return Result.error(Result.Code.E_DATA_ERROR, "文章id不匹配!");
        }

        JwtUser jwtUser = JwtUtils.getUser();
        //articleVo.setUid(jwtUser.getUid());

        ArticleVo resultVo = articleService.editArticle(articleVo);

        return Result.success(resultVo);
    }

    @RequiresPermissions("article:delete")
    @DeleteMapping("/delete")
    public Result delete(@RequestBody BatchIdVo batchIdVo) {
        List<Integer> ids = new ArrayList<>();        
        if (batchIdVo.getId() != null) {
            ids.add(batchIdVo.getId());
        } else {
            ids.addAll(batchIdVo.getIds());
        }

        int numrows = 0;
        for (Integer id : ids) {
            try {
                articleService.removeArticle(id);
                numrows++;
            } catch (Exception e) {}
            
        };
        
        Map<String, Object> data = new HashMap<>();
        data.put("success", numrows);
        data.put("fail", ids.size() - numrows);

        return Result.success(data);
    }


    @RequiresPermissions("article:publish")
    @PostMapping("/publish")
    public Result publish(@RequestBody BatchIdVo batchIdVo) {
         
        if (batchIdVo.getId() != null) {
            articleService.publishArticle(batchIdVo.getId());
            return Result.success(null);
        } else {
            List<Integer> ids = new ArrayList<>();       

            ids.addAll(batchIdVo.getIds());
            int numrows = 0;
            for (Integer id : ids) {
                try {
                    articleService.publishArticle(id);
                    numrows++;
                } catch (Exception e) {}
                
            };
            
            Map<String, Object> data = new HashMap<>();
            data.put("success", numrows);
            data.put("fail", ids.size() - numrows);

            return Result.success(data);
        }

        
    }

    @RequiresPermissions("article:audit")
    @PostMapping("/audit")
    public Result audit(@RequestBody AuditBatchIdVo batchIdVo) {
        String audit = batchIdVo.getAudit();
        if (!StringUtils.equals("pass", audit) && !StringUtils.equals("reject", audit)) {
            return Result.error(Result.Code.E_PARAM_ERROR, "audit参数不正确! pass|reject");
        }

        if (batchIdVo.getId() != null) {
            articleService.auditArticle(batchIdVo.getId(), batchIdVo.getAudit());
            return Result.success(null);
        } else {
            List<Integer> ids = new ArrayList<>(); 

            ids.addAll(batchIdVo.getIds());

            int numrows = 0;
            for (Integer id : ids) {
                try {
                    articleService.auditArticle(id, batchIdVo.getAudit());
                    numrows++;
                } catch (Exception e) {}
                
            };
            
            Map<String, Object> data = new HashMap<>();
            data.put("success", numrows);
            data.put("fail", ids.size() - numrows);
    
            return Result.success(data);
        }


    }
}

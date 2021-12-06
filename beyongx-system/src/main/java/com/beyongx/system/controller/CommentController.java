package com.beyongx.system.controller;

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
import com.beyongx.system.entity.CmsComment;
import com.beyongx.system.entity.meta.CommentMeta;
import com.beyongx.system.service.ICmsCommentService;
import com.beyongx.system.vo.AuditBatchIdVo;
import com.beyongx.system.vo.CommentVo;

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

@RestController
@RequestMapping("/api/comment")
@Slf4j
public class CommentController {
    @Autowired
    private ICmsCommentService commentService;
    
    @RequiresPermissions("comment:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<CmsComment> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        if (filters.containsKey("keyword")) {
            String keyword = (String)filters.remove("keyword");
            queryWrapper.like("content", keyword);
        }
        
        if (filters.containsKey("startTime")) {
            String startTime = (String)filters.remove("startTime");
            queryWrapper.ge("create_time", startTime);
        }
        if (filters.containsKey("endTime")) {
            String endTime = (String)filters.remove("endTime");
            queryWrapper.lt("create_time", endTime);
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

        IPage<CmsComment> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        
        IPage<CmsComment> pageList = commentService.page(page, queryWrapper);
        if (CollectionUtils.isEmpty(pageList.getRecords())) {
            return Result.success(pageList);
        }

        return Result.success(pageList);
    }

    @RequiresPermissions("comment:query")
    @GetMapping("/{id}")
    public Result query(@PathVariable(value="id") Integer id) {
        CmsComment comment = commentService.getById(id);
        if (comment == null || comment.getStatus() == CommentMeta.Status.DELETED.getCode()) {
            return Result.error(Result.Code.E_DATA_NOT_FOUND, "评论不存在!");
        }

        return Result.success(comment);
    }

    @RequiresPermissions("comment:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class}) @RequestBody CommentVo commentVo) {
        JwtUser jwtUser = JwtUtils.getUser();
        commentVo.setUid(jwtUser.getUid());
        
        if (commentVo.getPid() != null) {
            CmsComment pComment = commentService.getById(commentVo.getPid());
            if (pComment == null || pComment.getStatus() == CommentMeta.Status.DELETED.getCode()) {
                return Result.error(Result.Code.E_DATA_ERROR, "回复的评论不存在!");
            }
            if (pComment.getArticleId() != commentVo.getArticleId()) {
                return Result.error(Result.Code.E_DATA_VALIDATE_ERROR, "文章id验证错误!");
            }
        }

        CmsComment comment = commentService.createComment(commentVo);

        return Result.success(comment);
    }

    @RequiresPermissions("comment:audit")
    @PostMapping("/audit")
    public Result audit(@RequestBody AuditBatchIdVo batchIdVo) {
        String audit = batchIdVo.getAudit();
        if (!StringUtils.equals("pass", audit) && !StringUtils.equals("reject", audit)) {
            return Result.error(Result.Code.E_PARAM_ERROR, "audit参数不正确! pass|reject");
        }

        if (batchIdVo.getId() != null) {
            commentService.auditComment(batchIdVo.getId(), audit);
            return Result.success(null);
        } else {
            List<Integer> ids = new ArrayList<>(); 

            ids.addAll(batchIdVo.getIds());

            int numrows = 0;
            for (Integer id : ids) {
                try {
                    commentService.auditComment(id, audit);
                    numrows++;
                } catch (Exception e) {}
                
            };
            
            Map<String, Object> data = new HashMap<>();
            data.put("success", numrows);
            data.put("fail", ids.size() - numrows);
    
            return Result.success(data);
        }
    }

    @RequiresPermissions("comment:delete")
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
                commentService.removeComment(id);
                numrows++;
            } catch (Exception e) {}
            
        };
        
        Map<String, Object> data = new HashMap<>();
        data.put("success", numrows);
        data.put("fail", ids.size() - numrows);

        return Result.success(data);
    }
}

package com.beyongx.system.controller;


import com.beyongx.common.vo.Result;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @RequiresPermissions("article:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list() {
        return Result.success(null);
    }

    @RequiresPermissions("article:query")
    @GetMapping("/{id}")
    public Result query(@PathVariable(value="id") Integer id) {
        return Result.success(null);
    }

    @RequiresPermissions("article:list")
    @PostMapping("/create")
    public Result create() {
        return Result.success(null);
    }

    @RequiresPermissions("article:edit")
    @PostMapping("/edit")
    public Result edit() {
        return Result.success(null);
    }

    @RequiresPermissions("article:list")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        return Result.success(null);
    }


    @RequiresPermissions("article:publish")
    @PostMapping("/publish")
    public Result publish() {
        return Result.success(null);
    }

    @RequiresPermissions("article:audit")
    @PostMapping("/audit")
    public Result audit() {
        return Result.success(null);
    }
}

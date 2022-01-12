package com.beyongx.cms.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.system.entity.CmsArticle;
import com.beyongx.system.entity.CmsCategory;
import com.beyongx.system.service.ICmsArticleService;
import com.beyongx.system.service.ICmsCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ArticleController {
    
    @Autowired
    ICmsArticleService articleService;
    @Autowired
    ICmsCategoryService categoryService;

    /**
     * 所有文章列表
     * @param modelAndView
     * @return
     */
    @RequestMapping("/article/index")
    public ModelAndView index(ModelAndView modelAndView, @RequestParam(name="cid", defaultValue="0") Integer cid,
                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo, HttpServletRequest request) {
        IPage<CmsArticle> page = new Page<CmsArticle>(pageNo, 10);
        Map<String, Object> params = new HashMap<>();
        
        IPage<CmsArticle> pageList = articleService.listByCategoryId(page, cid, params);
        modelAndView.addObject("list", pageList);

        CmsCategory category = categoryService.getById(cid);
        modelAndView.addObject("category", category);

        modelAndView.setViewName("/cms/article/index");
        return modelAndView;
    }

    /**
     * 文章列表
     * @param modelAndView
     * @return
     */
    @RequestMapping("/article/list")
    public ModelAndView articleList(ModelAndView modelAndView, @RequestParam(name="cid", defaultValue="0") Integer cid,
                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo, HttpServletRequest request) {
        IPage<CmsArticle> page = new Page<CmsArticle>(pageNo, 10);
        Map<String, Object> params = new HashMap<>();
        
        IPage<CmsArticle> pageList = articleService.listByCategoryId(page, cid, params);
        modelAndView.addObject("list", pageList);

        CmsCategory category = categoryService.getById(cid);
        modelAndView.addObject("category", category);

        modelAndView.setViewName("/cms/article/list");
        return modelAndView;
    }

    /**
     * 文章明细
     * @param modelAndView
     * @return
     */
    @RequestMapping("/article/view")
    public ModelAndView viewArticle(ModelAndView modelAndView, @RequestParam(name="aid", defaultValue="0") Integer aid,
                                    @RequestParam(name="cid", defaultValue="0") Integer cid, HttpServletRequest request) {
        CmsArticle article = articleService.getById(aid);
        CmsCategory category = categoryService.getById(cid);

        modelAndView.addObject("article", article);
        modelAndView.addObject("category", category);

        // if (article.getAttachmentId() != null) {
        //     ScassFile file = scassFileService.getById(article.getAttachmentId());
        //     modelAndView.addObject("attachment", file);
        // }

        modelAndView.setViewName("/portal/article/viewArticle");
        return modelAndView;
    }

}

package com.beyongx.system.service.impl;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.system.entity.CmsArticle;
import com.beyongx.system.entity.CmsCategory;
import com.beyongx.system.entity.SysConfig;
import com.beyongx.system.entity.SysFile;
import com.beyongx.system.entity.meta.ArticleMeta;
import com.beyongx.system.mapper.CmsArticleMapper;
import com.beyongx.system.mapper.SysFileMapper;
import com.beyongx.system.service.ICmsArticleService;
import com.beyongx.system.service.ISysConfigService;
import com.beyongx.system.vo.ArticleVo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
@CacheConfig(cacheNames = "articleCache")
@Slf4j
public class CmsArticleServiceImpl extends ServiceImpl<CmsArticleMapper, CmsArticle> implements ICmsArticleService {
    @Autowired
    private SysFileMapper fileMapper;

    @Autowired
    private ISysConfigService configService;

    @Override
    @Cacheable(key = "#id",unless = "#result.status==-1")
    public ArticleVo getArticle(Integer id) {        
        CmsArticle article = baseMapper.selectById(id);
        if (article == null) {
            log.warn("文章id不存在!");
            throw new ServiceException(Result.Code.E_DATA_NOT_FOUND, "文章id不存在!");
        }

        ArticleVo articleVo = new ArticleVo();
        try {
            BeanUtils.copyProperties(articleVo, article);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        List<CmsCategory> categorys = this.listCategorys(article.getId());
        articleVo.setCategorys(categorys);

        Integer thumbImageId = article.getThumbImageId();
        SysFile thumbImage = fileMapper.selectById(thumbImageId);
        articleVo.setThumbImage(thumbImage);

        List<SysFile> metaImages = new ArrayList<>();
        articleVo.setMetaImages(metaImages);

        List<SysFile> metaFiles = new ArrayList<>();
        articleVo.setMetaFiles(metaFiles);

        return articleVo;
    }

    @Override
    @CachePut(key = "#result.id")
    public ArticleVo createArticle(ArticleVo articleVo) {
        CmsArticle article = new CmsArticle();
        try {
            BeanUtils.copyProperties(article, articleVo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        article.setStatus(ArticleMeta.Status.DRAFT.getCode());

        Date createTime = new Date();
        article.setCreateTime(createTime);
        article.setUpdateTime(createTime);

        // Integer numrows = baseMapper.insert(article);
        // if (numrows < 0) {
        //     log.warn("新增文章失败");
        //     throw new ServiceException(Result.Code.E_UNKNOWN_ERROR, "新增文章失败!");
        // }

        boolean success = this.save(article);
        if (!success) {
            log.warn("新增文章失败");
            throw new ServiceException(Result.Code.E_UNKNOWN_ERROR, "新增文章失败!");            
        }

        //更新category ids
        //更新meta image ids;
        //更新meta file ids;

        ArticleVo newArticleVo = this.getArticle(article.getId());
        return newArticleVo;
    }

    @Override
    @CachePut(key = "#result.id")
    public ArticleVo editArticle(ArticleVo articleVo) {
        CmsArticle article = new CmsArticle();
        try {
            BeanUtils.copyProperties(article, articleVo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        Date updateTime = new Date();
        article.setUpdateTime(updateTime);

        int numrows = baseMapper.updateById(article);
        if (numrows < 0) {
            log.warn("文章更新失败");
            throw new ServiceException(Result.Code.E_UNKNOWN_ERROR, "文章更新失败!");
        }
        
        //更新category ids
        //更新meta image ids;
        //更新meta file ids;

        ArticleVo newArticleVo = this.getArticle(articleVo.getId());

        return newArticleVo;
    }

    @Override
    @CacheEvict(key = "#id")
    public boolean removeArticle(Integer id) {
        QueryWrapper<CmsArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(CmsArticle.class, entity -> !entity.getColumn().equals("content"));
        queryWrapper.eq("id", id);

        CmsArticle article = baseMapper.selectOne(queryWrapper);
        if (article == null) {
            log.warn("文章id不存在!");
            throw new ServiceException(Result.Code.E_DATA_NOT_FOUND, "文章不存在!");
        }

        int numrows = baseMapper.deleteById(id);
        log.info("删除文章：{}", id);

        return numrows == 1;
    }
    
    @CacheEvict(key = "#id")
    @Override
    public boolean publishArticle(Integer id) {
        QueryWrapper<CmsArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(CmsArticle.class, entity -> !entity.getColumn().equals("content"));
        queryWrapper.eq("id", id);
        queryWrapper.ne("status", ArticleMeta.Status.DELETED.getCode());

        CmsArticle article = baseMapper.selectOne(queryWrapper);
        if (article == null) {
            log.warn("文章id不存在!");
            throw new ServiceException(Result.Code.E_DATA_NOT_FOUND, "文章不存在!");
        }

        if (article.getStatus() == ArticleMeta.Status.PUBLISHED.getCode()) {
            throw new ServiceException(Result.Code.E_DATA_VALIDATE_ERROR, "文章已发布!");
        }

        UpdateWrapper<CmsArticle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("update_time", new Date());

        SysConfig sysConfig = configService.getByGroupAndKey("article", "article_audit_switch");
        if (sysConfig != null && StringUtils.equals("false", sysConfig.getValue())) {
            updateWrapper.set("status", ArticleMeta.Status.PUBLISHED.getCode());
        } else {
            updateWrapper.set("status", ArticleMeta.Status.PUBLISHING.getCode());
        }

        int numrows = baseMapper.update(null, updateWrapper);

        return numrows == 1;
    }

    @CacheEvict(key = "#id")
    @Override
    public boolean auditArticle(Integer id, String audit) {
        if (!StringUtils.equals("pass", audit) && !StringUtils.equals("reject", audit)) {
            throw new ServiceException(Result.Code.E_PARAM_ERROR, "audit参数不正确! pass|reject");
        }

        QueryWrapper<CmsArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(CmsArticle.class, entity -> !entity.getColumn().equals("content"));
        queryWrapper.eq("id", id);

        CmsArticle article = baseMapper.selectOne(queryWrapper);
        if (article == null) {
            log.warn("文章id不存在!");
            throw new ServiceException(Result.Code.E_DATA_NOT_FOUND, "文章不存在!");
        }

        if (article.getStatus() != ArticleMeta.Status.PUBLISHING.getCode()) {
            throw new ServiceException(Result.Code.E_DATA_VALIDATE_ERROR, "文章未申请发布!");
        }

        UpdateWrapper<CmsArticle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("update_time", new Date());

        if (StringUtils.equals("pass", audit)) {
            updateWrapper.set("status", ArticleMeta.Status.PUBLISHED.getCode());
        } else if (StringUtils.equals("reject", audit)) {
            updateWrapper.set("status", ArticleMeta.Status.FIRST_AUDIT_REJECT.getCode());
        }        

        int numrows = baseMapper.update(null, updateWrapper);

        return numrows == 1;
    }

    @Override
    public List<CmsCategory> listCategorys(Integer aid) {
        return baseMapper.selectCategorysById(aid);
    }

}

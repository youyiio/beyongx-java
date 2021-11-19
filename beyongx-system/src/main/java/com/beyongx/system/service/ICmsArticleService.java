package com.beyongx.system.service;

import com.beyongx.system.entity.CmsArticle;
import com.beyongx.system.entity.CmsCategory;
import com.beyongx.system.vo.ArticleVo;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface ICmsArticleService extends IService<CmsArticle> {

    ArticleVo getArticle(Integer id);

    ArticleVo createArticle(ArticleVo articleVo);

    ArticleVo editArticle(ArticleVo articleVo);

    boolean removeArticle(Integer id);

    boolean publishArticle(Integer id);

    boolean auditArticle(Integer id, String audit);

    /** 关联表 start */

    List<CmsCategory> listCategorys(Integer aid);

    /** 关联表 end */
}

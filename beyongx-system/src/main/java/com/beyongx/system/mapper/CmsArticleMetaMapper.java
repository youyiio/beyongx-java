package com.beyongx.system.mapper;

import com.beyongx.system.entity.CmsArticleMeta;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 文章meta表 Mapper 接口
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface CmsArticleMetaMapper extends BaseMapper<CmsArticleMeta> {

    List<CmsArticleMeta> selectByMetaKey(Integer articleId, String metaKey);

    CmsArticleMeta selectOneByMetaKey(Integer articleId, String metaKey);
}

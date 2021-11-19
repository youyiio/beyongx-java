package com.beyongx.system.mapper;

import com.beyongx.system.entity.CmsArticle;
import com.beyongx.system.entity.CmsCategory;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface CmsArticleMapper extends BaseMapper<CmsArticle> {

    /** 关联表定义 start */

    List<CmsCategory> selectCategorysById(Integer id);

    /** 关联表定义 end */
}

package com.beyongx.system.service;

import com.beyongx.system.entity.CmsComment;
import com.beyongx.system.vo.CommentVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface ICmsCommentService extends IService<CmsComment> {

    CmsComment createComment(CommentVo commentVo);

    boolean auditComment(Integer id, String audit);

    boolean removeComment(Integer id);
}

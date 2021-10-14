package com.beyongx.framework.service.impl;

import com.beyongx.framework.entity.CmsComment;
import com.beyongx.framework.mapper.CmsCommentMapper;
import com.beyongx.framework.service.ICmsCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
public class CmsCommentServiceImpl extends ServiceImpl<CmsCommentMapper, CmsComment> implements ICmsCommentService {

}

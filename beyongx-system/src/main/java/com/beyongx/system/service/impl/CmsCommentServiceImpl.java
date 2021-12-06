package com.beyongx.system.service.impl;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.system.entity.CmsArticle;
import com.beyongx.system.entity.CmsComment;
import com.beyongx.system.entity.meta.CommentMeta;
import com.beyongx.system.entity.meta.MessageMeta;
import com.beyongx.system.mapper.CmsArticleMapper;
import com.beyongx.system.mapper.CmsCommentMapper;
import com.beyongx.system.service.ICmsCommentService;
import com.beyongx.system.service.ISysMessageService;
import com.beyongx.system.vo.CommentVo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CmsArticleMapper articleMapper;
    @Autowired
    private ISysMessageService messageService;

    @Override
    public CmsComment createComment(CommentVo commentVo) {
        CmsComment comment = new CmsComment();
        try {
            BeanUtils.copyProperties(comment, commentVo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        CmsArticle article = articleMapper.selectById(comment.getArticleId());
        if (article == null) {
            throw new ServiceException(Result.Code.E_DATA_NOT_FOUND, "文章不存在!");
        }

        comment.setStatus(CommentMeta.Status.DRAFT.getCode());

        Date currentDate = new Date();
        comment.setCreateTime(currentDate);

        boolean success = this.save(comment);
        if (!success) {
            log.warn("评论文章失败!");
            throw new ServiceException(Result.Code.E_UNKNOWN_ERROR, "评论文章失败!");     
        }

        //增加评论数量
        articleMapper.incCommentCount(comment.getArticleId());

        //发送评论消息
        String msgTitle = "新评论消息";
        String msgContent = comment.getAuthor() + "评论了文章 “" + article.getTitle() + "”";
        String from = comment.getUid().toString();
        String to = MessageMeta.UID.SYSTEM.getKey();
        String type = MessageMeta.Type.COMMENT.getKey();
        messageService.sendMessage(from, to, msgTitle, msgContent, type);

        return comment;
    }

    @Override
    public boolean auditComment(Integer id, String audit) {
        if (!StringUtils.equals("pass", audit) && !StringUtils.equals("reject", audit)) {
            throw new ServiceException(Result.Code.E_PARAM_ERROR, "audit参数不正确! pass|reject");
        }

        CmsComment comment = baseMapper.selectById(id);
        if (comment == null) {
            throw new ServiceException(Result.Code.E_DATA_NOT_FOUND, "评论不存在!");
        }

        if (StringUtils.equals("pass", audit)) {
            comment.setStatus(CommentMeta.Status.PUBLISHED.getCode());
        } else if (StringUtils.equals("reject", audit)) {
            comment.setStatus(CommentMeta.Status.REFUSE.getCode());
        }

        baseMapper.updateById(comment);
        
        return true;
    }

    @Override
    public boolean removeComment(Integer id) {
        UpdateWrapper<CmsComment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", CommentMeta.Status.DELETED.getCode());
        updateWrapper.eq("id", id);
        baseMapper.update(null, updateWrapper);
        
        return true;
    }

}

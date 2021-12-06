package com.beyongx.system.service.impl;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.system.entity.SysMessage;
import com.beyongx.system.entity.meta.MessageMeta;
import com.beyongx.system.mapper.SysMessageMapper;
import com.beyongx.system.service.ISysMessageService;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

    @Override
    public SysMessage sendMessage(String from, String to, String title, String content, String type) {
        if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
            throw new ServiceException(Result.Code.E_DATA_ERROR, "发送消息的来源或去向不能为空!");
        }
        if (StringUtils.isBlank(title)) {
            throw new ServiceException(Result.Code.E_DATA_ERROR, "发送消息的标题不能为空!");
        }

        SysMessage message = new SysMessage();
        message.setFromUid(from);
        message.setToUid(to);
        message.setTitle(title);
        message.setContent(content);
        
        if (type == null) {
            type = MessageMeta.Type.MAIL.getKey();
        }
        message.setType(type);
        message.setStatus(MessageMeta.Status.SEND.getCode());
        message.setIsReaded(false);

        Date currentTime = new Date();
        message.setSendTime(currentTime);
        message.setCreateTime(currentTime);

        boolean success = this.save(message);
        if (!success) {
            throw new ServiceException(Result.Code.E_UNKNOWN_ERROR, "消息保存失败!");
        }

        return message;
    }

    @Override
    public boolean setReaded(Integer id) {
        UpdateWrapper<SysMessage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_readed", true).set("read_time", new Date());
        updateWrapper.eq("id", id);

        boolean success = this.update(updateWrapper);
        return success;
    }

    @Override
    public boolean setAllReaded(String type) {
        UpdateWrapper<SysMessage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_readed", true).set("read_time", new Date());
        updateWrapper.eq("type", type).eq("is_readed", false);

        boolean success = this.update(updateWrapper);
        return success;
    }

}

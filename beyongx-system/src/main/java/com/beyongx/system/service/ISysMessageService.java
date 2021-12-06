package com.beyongx.system.service;

import com.beyongx.system.entity.SysMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 消息表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface ISysMessageService extends IService<SysMessage> {

    SysMessage sendMessage(String from, String to, String title, String content, String type);

    boolean setReaded(Integer id);

    boolean setAllReaded(String type);
}

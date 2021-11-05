package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysActionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作类型
     */
    private String action;

    /**
     * 用户名
     */
    private String username;

    /**
     * 模块
     */
    private String module;

    /**
     * 组件
     */
    private String component;

    private String ip;

    private Long actionTime;

    private String params;

    /**
     * 用户代理
     */
    private String userAgent;

    private String response;

    private Long responseTime;

    private String remark;

    private Date createTime;


}

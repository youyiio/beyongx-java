package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
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
     * 操作模块
     */
    private String module;

    private String component;

    private String ip;

    private Long actionTime;

    private String params;

    private String response;

    private Long responseTime;

    private String remark;

    private Date createTime;


}

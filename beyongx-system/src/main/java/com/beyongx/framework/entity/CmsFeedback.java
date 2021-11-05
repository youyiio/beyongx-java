package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 意见反馈表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmsFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String content;

    private Integer status;

    private String sendClientId;

    private String replyClientId;

    private Long replyFeedbackId;

    private String ip;

    /**
     * 发送可能来自网页、app或微信等
     */
    private String source;

    private Date sendTime;

    private Date readTime;

    private Date replyTime;

    private Date createTime;


}

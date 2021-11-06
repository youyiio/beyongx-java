package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String type;

    private String title;

    private String content;

    /**
     * -1.删除.0.草稿;1.提交;2.已发送;
     */
    private Integer status;

    private String fromUid;

    private String toUid;

    private Date sendTime;

    private Boolean isReaded;

    private Date readTime;

    private String ext;

    private Date createTime;


}

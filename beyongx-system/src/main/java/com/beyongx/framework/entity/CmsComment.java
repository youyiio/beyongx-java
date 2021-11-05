package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmsComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer pid;

    private String content;

    /**
     * -1,删除;0.草稿;1.申请发布;2.拒绝;3.发布
     */
    private Integer status;

    private String author;

    private String authorEmail;

    private String authorUrl;

    private String ip;

    private Integer uid;

    private Integer articleId;

    private Date createTime;


}

package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmsArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String keywords;

    private String description;

    private String content;

    private Date postTime;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Boolean isTop;

    private Integer thumbImageId;

    private Integer readCount;

    private Integer commentCount;

    private String author;

    private Integer uid;

    private Integer sort;


}

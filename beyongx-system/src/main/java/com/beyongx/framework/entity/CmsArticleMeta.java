package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章meta表
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmsArticleMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer targetId;

    private String metaKey;

    private String metaValue;

    private Date updateTime;

    private Date createTime;


}

package com.beyongx.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章相关表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmsArticleData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer articleAId;

    private Integer articleBId;

    private Float titleSimilar;

    private Float contentSimilar;

    private Date updateTime;

    private Date createTime;


}

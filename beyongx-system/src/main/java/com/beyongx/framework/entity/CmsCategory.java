package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 分类表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmsCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String pid;

    private String title;

    private String name;

    private String remark;

    /**
     * 0.下线;1.上线
     */
    private Integer status;

    private Integer sort;

    private Date createTime;


}

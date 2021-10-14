package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 链接
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmsLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String url;

    private Integer sort;

    private Integer status;

    private Date createTime;


}

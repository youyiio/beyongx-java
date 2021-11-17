package com.beyongx.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 上级部门
     */
    private Integer pid;

    /**
     * 标识
     */
    private String name;

    /**
     * 名称
     */
    private String title;

    /**
     * 启用状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;


}

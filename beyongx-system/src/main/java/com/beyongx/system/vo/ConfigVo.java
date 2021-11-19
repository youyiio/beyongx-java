package com.beyongx.system.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ConfigVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典组
     */
    private String group;

    /**
     * 字典键
     */
    private String key;

    /**
     * 字典值
     */
    private String value;

    /**
     * 值类型
     */
    private String valueType;

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

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}

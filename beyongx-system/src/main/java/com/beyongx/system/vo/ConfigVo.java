package com.beyongx.system.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.beyongx.common.validation.group.Create;
import com.beyongx.common.validation.group.Edit;

import lombok.Data;

@Data
public class ConfigVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "字典id不能为空!", groups = {Edit.class})
    private Integer id;

    @NotBlank(message = "字典名称不能为空!", groups = {Create.class, Edit.class})
    private String name;

    @NotBlank(message = "字典组不能为空!", groups = {Create.class, Edit.class})
    private String group;

    @NotBlank(message = "字典键不能为空!", groups = {Create.class, Edit.class})
    private String key;

    @NotBlank(message = "字典值不能为空!", groups = {Create.class, Edit.class})
    private String value;

    @NotBlank(message = "字典值类型不能为空!", groups = {Create.class, Edit.class})
    @Pattern(message = "字典值类型只能为integer、float、string、text或boolean", regexp = "integer|float|string|text|boolean", groups = {Create.class, Edit.class})
    private String valueType;

    private Integer status;

    @NotNull(message = "排序不能为空!", groups = {Create.class, Edit.class})
    private Integer sort;

    private String remark;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;
}

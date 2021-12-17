package com.beyongx.system.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.beyongx.common.validation.group.Create;
import com.beyongx.common.validation.group.Edit;
import com.beyongx.common.vo.Node;

import lombok.Data;

@Data
public class DeptVo extends Node implements Serializable {
    private static final long serialVersionUID = 1L;

    // @NotNull(message = "部门id不能为空!", groups = {Edit.class})
    // private Integer id;

    @NotNull(message = "部门pid不能为空!", groups = {Create.class, Edit.class})
    private Integer pid;

    @NotBlank(message = "部门标识不能为空!", groups = {Create.class, Edit.class})
    @Pattern(message = "部门标识格式为字母、数字、-(横杆)和_(下横杆)的3-64位组合!", regexp = "[a-zA-Z0-9\\-_]{3,64}", groups = {Create.class, Edit.class})
    private String name;

    @NotBlank(message = "部门标题不能为空!", groups = {Create.class, Edit.class})
    private String title;

    private Integer status;

    @NotNull(message = "部门排序不能为空!", groups = {Create.class, Edit.class})
    private Integer sort;

    private String remark;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;

}

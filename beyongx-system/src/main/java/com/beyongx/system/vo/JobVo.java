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
public class JobVo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "岗位id不能为空!", groups = {Edit.class})
    private Integer id;

    @NotBlank(message = "岗位标识不能为空!", groups = {Create.class, Edit.class})
    @Pattern(message = "岗位标识格式为字母、数字、-(横杆)和_(下横杆)的3-64位组合!", regexp = "[a-zA-Z0-9\\-_]{3,64}", groups = {Create.class, Edit.class})
    private String name;

    @NotBlank(message = "岗位标题不能为空!", groups = {Create.class, Edit.class})
    private String title;

    private Integer status;

    @NotNull(message = "岗位排序不能为空!", groups = {Create.class, Edit.class})
    private Integer sort;

    private String remark;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;
}

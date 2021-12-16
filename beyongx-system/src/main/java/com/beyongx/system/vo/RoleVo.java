package com.beyongx.system.vo;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.beyongx.common.validation.group.Always;
import com.beyongx.common.validation.group.Edit;

import lombok.Data;

@Data
public class RoleVo {
    
    @NotNull(message = "角色id不能为空!", groups = {Edit.class})
    private Integer id;

    @NotBlank(message = "角色标识不能为空!", groups = {Always.class})
    private String name;

    @NotBlank(message = "角色名称不能为空!", groups = {Always.class})
    private String title;

    private Integer status;

    @NotNull(message = "备注不能为null!", groups = {Always.class})
    private String remark;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;


    private List<Integer> menuIds;
}

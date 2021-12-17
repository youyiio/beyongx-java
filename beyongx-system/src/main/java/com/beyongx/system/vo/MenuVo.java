package com.beyongx.system.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.beyongx.common.validation.group.Create;
import com.beyongx.common.validation.group.Edit;
import com.beyongx.common.vo.Node;

import lombok.Data;

@Data
public class MenuVo extends Node implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //private Integer id; //Node已经包含id
    
    @NotBlank(message ="菜单标题不能为空", groups = {Create.class, Edit.class})
    private String title;

    //@NotBlank(message ="组件名称不能为空", groups = {Create.class})
    private String name;

    //@NotBlank(message ="组件地址不能为空", groups = {Create.class})
    private String component;

    @NotBlank(message ="路由地址不能为空", groups = {Create.class, Edit.class})
    private String path;

    private String icon;

    @NotNull(message = "菜单类型不能为空", groups = {Create.class, Edit.class})
    private Integer type;

    @NotNull(message = "是否菜单不能为空", groups = {Create.class, Edit.class})
    private Boolean isMenu;

    private String permission;

    private Integer status;

    @NotBlank(message ="归属模块不能为空", groups = {Create.class, Edit.class})
    private String belongsTo;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;
}

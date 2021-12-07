package com.beyongx.system.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.beyongx.common.validation.group.Always;
import com.beyongx.common.vo.Node;

import lombok.Data;

@Data
public class CategoryVo extends Node implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "分类标题不能为空!", groups = {Always.class})
    private String title;

    @NotBlank(message = "分类名称标识不能为空!", groups = {Always.class})
    private String name;

    private String remark;

    /**
     * 0.下线;1.上线
     */
    private Integer status;

    private Integer sort;

    private Date createTime;
}

package com.beyongx.system.vo;

import java.io.Serializable;
import java.sql.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.beyongx.common.validation.group.Create;
import com.beyongx.common.validation.group.Edit;

import lombok.Data;

@Data
public class LinkVo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "友链id不能为空!", groups = {Edit.class})
    private Integer id;

    @NotBlank(message = "标题不能为空!", groups = {Create.class, Edit.class})
    private String title;

    @NotBlank(message = "链接url不能为空!", groups = {Create.class, Edit.class})
    private String url;

    @NotNull(message = "排序不能为空!", groups = {Create.class, Edit.class})
    private Integer sort;

    private Integer status;

    @NotNull(message = "起始时间不能为空!", groups = {Create.class, Edit.class})
    private Date startTime;

    @NotNull(message = "结束时间不能为空!", groups = {Create.class, Edit.class})
    private Date endTime;

    private Date createTime;

}

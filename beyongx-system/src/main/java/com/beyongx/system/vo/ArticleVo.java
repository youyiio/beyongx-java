package com.beyongx.system.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.beyongx.common.validation.group.Always;
import com.beyongx.common.validation.group.Edit;
import com.beyongx.system.entity.CmsCategory;
import com.beyongx.system.entity.SysFile;

import lombok.Data;

@Data
public class ArticleVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "文章id不能为空", groups = {Edit.class})
    private Integer id;

    @NotBlank(message = "标题不能为空", groups = {Always.class})
    private String title;

    private String keywords;

    private String description;

    @NotBlank(message = "内容不能为空", groups = {Always.class})
    private String content;

    private Date postTime;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Boolean isTop;

    private Integer thumbImageId;

    private Integer readCount;

    private Integer commentCount;

    private String author;

    private Integer uid;

    private Integer sort;

    private String relateds;

    @NotEmpty(message = "文章分类不能为空", groups = {Always.class})
    private List<Integer> categoryIds;

    private List<String> tags;
    
    private List<Integer> metaImageIds;

    private List<Integer> metaFileIds;


    /** 关联表 start */
    private List<CmsCategory> categorys;

    private SysFile thumbImage;

    private List<SysFile> metaImages;

    private List<SysFile> metaFiles;
    
    /** 关联表 end */
}

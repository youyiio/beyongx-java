package com.beyongx.system.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.beyongx.common.validation.group.Always;

import lombok.Data;

@Data
public class CommentVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer pid;

    @NotBlank(message = "评论内容不能为空!", groups = {Always.class})
    private String content;

    private Integer status;

    private String author;

    private String authorEmail;

    private String authorUrl;

    private String ip;

    private Integer uid;

    @NotEmpty(message = "文章id不能为空!", groups = {Always.class})
    private Integer articleId;

    private Date createTime;
}

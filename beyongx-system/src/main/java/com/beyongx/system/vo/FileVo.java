package com.beyongx.system.vo;

import java.util.Date;

import lombok.Data;

@Data
public class FileVo {
    
    private Integer id;

    private String fileUrl;

    private String filePath;

    private String name;

    private String realName;

    private Long size;

    private String ext;

    private String bucket;

    private String ossUrl;

    private String thumbImageUrl;

    private String remark;

    private String createBy;

    private Date createTime;
}

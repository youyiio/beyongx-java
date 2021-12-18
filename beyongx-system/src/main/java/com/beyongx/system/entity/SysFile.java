package com.beyongx.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String fileUrl;

    /**
     * file_ulr所在目录
     */
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

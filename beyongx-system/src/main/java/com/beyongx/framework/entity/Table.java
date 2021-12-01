package com.beyongx.framework.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Table implements Serializable {
    
    private String tableCatalog;

    /**
     * 数据库名
     */
    private String tableSchema;

    /**
     * 表名
     */
    private String tableName;

    private String tableType;

    /**
     * 表引擎
     */
    private String engine; 

    private Integer version;

    private String rowFormat;

    /**
     * 数据行数
     */
    private Integer tableRows;

    /**
     * 平均每行长度
     */
    private Integer avgRowLength;

    private Long dataLength;

    private Long maxDataLength;

    /**
     * 索引长度
     */
    private Long indexLength;

    private Long dataFree;

    private Integer autoIncrement;

    private Date createTime;

    private Date checkTime;

    private String tableCollation;

    private String checkSum;

    private String createOptions;

    /**
     * 表备注
     */
    private String tableComment;
}

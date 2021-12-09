package com.beyongx.system.vo;

import java.util.Date;

import com.beyongx.system.entity.CmsAdSlot;

import lombok.Data;

@Data
public class AdServingVo {
    
    private Integer id;

    private Integer adId;

    private Integer slotId;

    /**
     * 0.下线;1.上线
     */
    private Integer status;

    private Integer sort;

    private Date startTime;

    private Date endTime;

    private Date updateTime;

    private Date createTime;

    private CmsAdSlot slot;
}

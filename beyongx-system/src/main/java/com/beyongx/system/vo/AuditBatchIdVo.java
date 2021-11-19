package com.beyongx.system.vo;

import java.util.List;

import lombok.Data;

@Data
public class AuditBatchIdVo {
    
    private Integer id;

    private List<Integer> ids;

    private String audit;
}

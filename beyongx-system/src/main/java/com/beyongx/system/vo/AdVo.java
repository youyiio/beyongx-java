package com.beyongx.system.vo;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.beyongx.common.validation.group.Always;
import com.beyongx.system.entity.CmsAdSlot;

import lombok.Data;

@Data
public class AdVo {
    
    public interface Create {

    }

    public interface Edit {

    }

    @NotNull(message = "广告id不能为空", groups = {Edit.class})
    @Pattern(message = "广告id为整数", regexp="^\\d+$", groups = {Edit.class})
    private Integer id;

    @NotBlank(message = "广告标题不能为空!", groups = {Always.class})
    private String title;

    @NotBlank(message = "广告链接不能为空!", groups = {Always.class})
    private String url;

    private Integer imageId;

    private Integer sort;

    private Date createTime;

    private Integer status;

    @NotEmpty(message = "广告插槽不能为空!", groups = {Always.class})
    private List<Integer> slotIds;

    private List<CmsAdSlot> slots;

    private Date startTime;

    private Date endTime;
}

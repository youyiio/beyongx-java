package com.beyongx.system.vo;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UcenterVo {
    
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    private String description;

    private String qq;

    private String weixin;
}

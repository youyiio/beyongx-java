package com.beyongx.framework.vo;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class PageVo {
    
    //页码
    @Digits(message = "页码为数字格式", integer = 6, fraction = 0)
    @Min(message = "页码必须大于0", value = 1)
    private Integer page = 1;

    //每页数量
    @Digits(message = "每页数量为数字格式", integer = 6, fraction = 0)
    @Min(message = "每页数量必须大于0", value = 1)
    private Integer size = 20;

    //分页的参数或过滤参数,内部参数皆可选
    private Map<String, Object> filters = new HashMap<>();

    //排序方式或组合,内部参数皆可
    private Map<String, String> orders = new HashMap<>();
}

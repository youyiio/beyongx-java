package com.beyongx.framework.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.vo.Result;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Demo接口 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/transaction")
public class DemoController {

    @GetMapping("/list")
    public Result transactionList() {
        Page<Map<String, Object>> page = new Page<>(1, 10);

        List<Map<String, Object>> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Map<String, Object> order = new HashMap<>();
            order.put("order_no", RandomStringUtils.randomNumeric(8));
            order.put("price", RandomStringUtils.randomNumeric(3));
            order.put("status", i % 3);

            list.add(order);
        }

        page.setTotal((int)Math.ceil(list.size() / 10.0));
        page.setRecords(list);

        return Result.success(page);
    }

    @GetMapping("/search/user")
    public Result searchUser() {
        String matchNames[] = {"beyongx", "beyongcms"};
        return Result.success(matchNames);
    }
}

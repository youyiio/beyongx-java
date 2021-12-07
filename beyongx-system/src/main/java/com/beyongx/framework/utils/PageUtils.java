package com.beyongx.framework.utils;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 已知列表进行分页
 */
public class PageUtils {
    
    public static <T> IPage<T> getPages(Integer page, Integer size, List<T> list) {
        IPage<T> ipage = new Page<>();
        int total = list.size();

        // if (size > total) {
        //     size = total;
        // }

        // 求出最大页数，防止page越界
        int maxPage = total % size == 0 ? total / size : total / size + 1;

        if (page > maxPage) {
            page = maxPage;
        }

        // 当前页第一条数据的下标
        int curIdx = page > 1 ? (page - 1) * size : 0;

        List<T> pageList = new ArrayList<>();

        // 将当前页的数据放进pageList
        for (int i = 0; i < size && curIdx + i < total; i++) {
            pageList.add(list.get(curIdx + i));
        }

        ipage.setCurrent(page);
        ipage.setSize(size);
        ipage.setTotal(total);
        ipage.setRecords(pageList);

        return ipage;
    }
}

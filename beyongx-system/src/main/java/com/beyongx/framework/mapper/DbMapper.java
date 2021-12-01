package com.beyongx.framework.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beyongx.framework.entity.Table;

public interface DbMapper extends BaseMapper<Table> {
    
    List<Map<String, Object>> selectAllDatabases();

    IPage<Table> selectAllTables(IPage<Table> page, String database);
}

package com.beyongx.framework.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beyongx.framework.entity.Table;

public interface IDbService {
    
    List<Map<String, Object>> listDatabases();
    
    IPage<Table> listTables(IPage<Table> page, String database);
}

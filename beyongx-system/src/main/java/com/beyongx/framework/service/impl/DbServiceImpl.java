package com.beyongx.framework.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beyongx.framework.entity.Table;
import com.beyongx.framework.mapper.DbMapper;
import com.beyongx.framework.service.IDbService;

import org.springframework.stereotype.Service;

@Service
public class DbServiceImpl extends ServiceImpl<DbMapper, Table> implements IDbService {

    @Override
    public List<Map<String, Object>> listDatabases() {
        List<Map<String, Object>> list = baseMapper.selectAllDatabases();
        list.stream().forEach(vo -> {
            vo.put("database", vo.remove("Database"));
        });
        
        return list;
    }

    @Override
    public IPage<Table> listTables(IPage<Table> page, String database) {

        return baseMapper.selectAllTables(page, database);
    }

    
}

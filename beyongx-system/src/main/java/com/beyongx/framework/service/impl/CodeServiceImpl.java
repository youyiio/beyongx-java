package com.beyongx.framework.service.impl;

import com.beyongx.framework.service.ICodeService;

import org.springframework.stereotype.Service;

@Service
public class CodeServiceImpl implements ICodeService {

    @Override
    public boolean sendCodeByMobile(String mobile, String type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean sendCodeByEmail(String mobile, String type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean checkCode(String type, String username, String code) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean consumeCode(String type, String username, String code) {
        // TODO Auto-generated method stub
        return false;
    }
    
}

package com.beyongx.framework.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName JwtToken
 * @Description
 * @Author youyi.io
 * @Date 2020/5/29 14:09
 * @Version 1.0
 **/
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken() {}
    
    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
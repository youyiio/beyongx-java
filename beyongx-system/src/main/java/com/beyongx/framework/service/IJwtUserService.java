package com.beyongx.framework.service;

import java.io.Serializable;
import java.util.List;

import com.beyongx.framework.shiro.JwtPermission;
import com.beyongx.framework.shiro.JwtRole;
import com.beyongx.framework.shiro.JwtUser;

public interface IJwtUserService {
    
    JwtUser getByUId(Integer uid);

    List<JwtRole> findByUid(Integer uid);

    List<JwtPermission> findByRoleId(Integer roleId);
}

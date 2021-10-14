package com.beyongx.framework.shiro;

import java.util.List;

/**
 *
 * JWT用户
 * @Author youyi.io
 * @Date 2020/5/29 13:16
 * @Version 1.0
 **/
public class JwtUser {
    private Integer uid;
    private String username;
    private String password;
    private String salt; //盐值
    private List<JwtRole> roleList;

    public JwtUser() {
    }

    public JwtUser(Integer uid, String username, String password, String salt, List<JwtRole> roleList) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.roleList = roleList;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<JwtRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<JwtRole> roleList) {
        this.roleList = roleList;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}

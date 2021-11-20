package com.beyongx.framework.shiro;

import java.io.Serializable;

/**
 *
 * 权限
 * @Author youyi.io
 * @Date 2020/5/29 13:35
 * @Version 1.0
 **/
public class JwtPermission implements Serializable {
    
    private Integer id; //权限id

    private String name; //权限名称

    private String path; //权限路径

    public JwtPermission() {}
    
    public JwtPermission(Integer id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
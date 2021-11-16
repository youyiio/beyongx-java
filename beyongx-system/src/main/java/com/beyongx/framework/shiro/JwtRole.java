package com.beyongx.framework.shiro;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName UserRole
 * @Description 角色表
 * @Author youyi.io
 * @Date 2020/5/29 13:34
 * @Version 1.0
 **/
public class JwtRole implements Serializable {

    private Integer roleId;

    private String roleName;
    
    List<JwtPermission> permissionList;

    public JwtRole(Integer roleId, String roleName, List<JwtPermission> permissionList) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.permissionList = permissionList;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<JwtPermission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<JwtPermission> permissionList) {
        this.permissionList = permissionList;
    }
}

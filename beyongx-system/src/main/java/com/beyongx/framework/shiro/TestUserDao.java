package com.beyongx.framework.shiro;


import java.util.ArrayList;
import java.util.List;

/**
 * 模拟用户列表
 * @Author youyi.io
 * @Date 2020/5/29 16:15
 * @Version 1.0
 **/
public class TestUserDao {

    public static List<JwtUser> userList = new ArrayList<>();
    static {
        ArrayList<JwtPermission> permissionList1 = new ArrayList<>();
        permissionList1.add(new JwtPermission(1,"shop", "/shop"));
        permissionList1.add(new JwtPermission(2,"game", "/"));
        permissionList1.add(new JwtPermission(3,"order", "/"));
        permissionList1.add(new JwtPermission(4,"address", "/"));

        ArrayList<JwtRole> userRoleArrayList1 = new ArrayList<>();
        userRoleArrayList1.add(new JwtRole(1, "老板", permissionList1));
        JwtUser user1 = new JwtUser(1, "admin", "123456", "iwejfiwjf", userRoleArrayList1);

        ArrayList<JwtPermission> permissionList2 = new ArrayList<>();
        permissionList2.add(new JwtPermission(1,"shop", "/"));
        permissionList2.add(new JwtPermission(2,"game", "/"));
        permissionList2.add(new JwtPermission(3,"order", "/"));
        ArrayList<JwtRole> userRoleArrayList2 = new ArrayList<>();
        userRoleArrayList2.add(new JwtRole(2, "经理", permissionList2));
        JwtUser user2 = new JwtUser(2, "xiaoming", "123456", "jfiosjfos", userRoleArrayList2);

        userList.add(user1);
        userList.add(user2);
    }

    public JwtUser query(String username, String token) {
        JwtUser user = null;

        for (JwtUser u : userList) {
            if (u.getUsername().equals(username)) {
                user = u;
            }
        }

        return user;
    }
}

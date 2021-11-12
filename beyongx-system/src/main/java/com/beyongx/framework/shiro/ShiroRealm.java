package com.beyongx.framework.shiro;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysUser;
import com.beyongx.system.service.ISysMenuService;
import com.beyongx.system.service.ISysUserService;
import com.beyongx.framework.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShiroRealm extends AuthorizingRealm {
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthorizingRealm.class);

    @Autowired
    @Lazy
    private ISysUserService userService;

    @Autowired
    @Lazy
    private ISysMenuService menuService;

    @Autowired
    @Lazy
    private RedisUtils redisUtils;

    /**
     * 重写，绕过身份令牌异常导致的shiro报错
     *
     * @param authenticationToken
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }

    /**
     * 1.执行认证逻辑; 用户及token验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException, TokenExpiredException {
        logger.info("1.执行认证逻辑");

        //获得token
        String token = (String) authenticationToken.getCredentials();
        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationException("token is blank!");
        }

        //获得token中的用户信息
        String username = JwtUtils.getUsername(token);
        //判空
        if (StringUtils.isBlank(username)) {
            throw new AuthenticationException("E_TOKEN_EXPIRED");
        }

        JwtUser jwtUser = null;
        try {
            SysUser user = userService.findByAccount(username);
            //查询用户是否存在
            if (user == null) {
                throw new AuthenticationException("E_TOKEN_INVALID");
            }

            jwtUser = new JwtUser();
            jwtUser.setUid(user.getId());
            jwtUser.setUsername(username);
            jwtUser.setSalt(user.getSalt());
            //token过期
            boolean isVerify = JwtUtils.verify(token, jwtUser);
            if (!isVerify) {
                throw new AuthenticationException("E_TOKEN_INVALID");
            }

            //拉取角色并填充JwtUser
            pullRolesAndPadding(jwtUser);
        } catch (TokenExpiredException e) {
            logger.warn("throw TokenExpiredException", e);

            throw new AuthenticationException("E_TOKEN_EXPIRED");
        } catch (Exception e) {
            logger.warn("throw AuthenticationException", e);

            throw e;
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(jwtUser, token, getName());
        return authenticationInfo;
    }

    /**
     * 2.用户角色权限认证（@RequiresPermissions 注释是需要角色的权限）
     * @param principals
     * @return 返回用户角色的权限列表
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("2.用户角色权限认证");

        //获取用户登录信息
        JwtUser user = (JwtUser) principals.getPrimaryPrincipal();

        // 设置角色和权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for (JwtRole userRole : user.getRoleList()) {
            authorizationInfo.addRole(userRole.getRoleName());
            for (JwtPermission permissionVo : userRole.getPermissionList()) {
                authorizationInfo.addStringPermission(permissionVo.getName());
            }
        }

        //返回用户的角色及权限列表
        return authorizationInfo;
    }

    /**
     * 拉取角色并填充JwtUser
     * @param jwtUser
     */
    private void pullRolesAndPadding(JwtUser jwtUser) {
        // 查询系统角色，建议缓存（如redis)
        List<SysRole> sysRoleList = userService.listRoles(jwtUser.getUid());

        List<JwtRole> roleList = new ArrayList<>();
        for (SysRole sysRole : sysRoleList) {

            //查询用户权限
            List<SysMenu> sysMenuList = menuService.listMenus(sysRole.getId());
            List<JwtPermission> permissionList = new ArrayList<>();
            for (SysMenu sysMenu : sysMenuList) {
                JwtPermission permission = new JwtPermission(sysMenu.getId(), sysMenu.getName(), sysMenu.getPath());
                permissionList.add(permission);
            }

            JwtRole role = new JwtRole(sysRole.getId(), sysRole.getName(), permissionList);
            roleList.add(role);
        }

        jwtUser.setRoleList(roleList);
    }
}
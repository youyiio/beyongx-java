package com.beyongx.framework.shiro;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.beyongx.framework.config.ShiroConfig;
import com.beyongx.framework.service.IJwtUserService;
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

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private ShiroConfig shiroConfig;

    @Autowired
    @Lazy
    private IJwtUserService userService;

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
        log.info("1.执行认证逻辑");

        //获得token
        String token = (String) authenticationToken.getCredentials();
        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationException("token is blank!");
        }

        //获得token中的用户信息
        JwtUser jwtUser = JwtUtils.decode(token);
        //判空
        if (jwtUser == null || jwtUser.getUid() == null || jwtUser.getUid() <= 0) {
            throw new AuthenticationException("E_TOKEN_EXPIRED");
        }

        try {
            //token过期
            boolean isVerify = JwtUtils.verify(token, jwtUser, shiroConfig.getSecret());
            if (!isVerify) {
                throw new AuthenticationException("E_TOKEN_INVALID");
            }

            Integer uid = jwtUser.getUid();
            jwtUser = userService.getByUId(uid);            
            //查询用户是否存在
            if (jwtUser == null) {
                throw new AuthenticationException("E_TOKEN_INVALID");
            }
        } catch (TokenExpiredException e) {
            log.warn("throw TokenExpiredException", e);

            throw new AuthenticationException("E_TOKEN_EXPIRED");
        } catch (Exception e) {
            log.warn("throw AuthenticationException", e);

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
        log.info("2.用户角色权限认证");

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

}
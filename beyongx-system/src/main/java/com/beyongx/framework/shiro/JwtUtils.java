package com.beyongx.framework.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName JWTUtil
 * @Description
 * @Author youyi.io
 * @Date 2020/5/29 13:18
 * @Version 1.0
 **/
public class JwtUtils {

    // 过期时间60分钟
    public static final long EXPIRE_TIME = 60 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param jwtUser JwtUser封装对象
     * @return 是否正确
     */
    public static boolean verify(String token, JwtUser jwtUser) {
        try {
            token = toVerifyToken(token);
            Algorithm algorithm = Algorithm.HMAC256(jwtUser.getSalt());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("uid", jwtUser.getUid())
                    .withClaim("username", jwtUser.getUsername())
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (TokenExpiredException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            token = toVerifyToken(token);
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static JwtUser getUser() {
        JwtUser user = (JwtUser) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    /**
     * 生成签名, {EXPIRE_TIME}后过期
     *
     * @param jwtUser JwtUser封装对象
     * @return 加密的token
     */
    public static String sign(JwtUser jwtUser) {

        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(jwtUser.getSalt());
        // 附带username信息
        return "Bear " + JWT.create()
                .withClaim("uid", jwtUser.getUid())
                .withClaim("username", jwtUser.getUsername())
                .withExpiresAt(date)
                .sign(algorithm);

    }

    public static String getRequestToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private static String toVerifyToken(String token) {
        return token != null ? token.replace("Bear ", "") : null;
    }
}
package com.beyongx.framework.shiro;


import com.beyongx.common.vo.Result;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
public class ShiroFilter extends AuthenticatingFilter {
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthorizingRealm.class);

    private  Gson gson = new Gson();

    private Result responseResult = Result.error(Result.Code.ACTION_FAILED, "验证失败");


    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        return null;
    }

    /**
     * 在这里拦截所有请求
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String token = JwtUtils.getRequestToken((HttpServletRequest)request);
        if (StringUtils.isBlank(token)) {
            // cookie中未检查到token或token为空
            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            String httpMethod = httpServletRequest.getMethod();
            String requestURI = httpServletRequest.getRequestURI();
            responseResult = Result.error(Result.Code.E_PARAM_ERROR, "Token emtpy");
            logger.info("请求 {} 的Token为空 请求类型 {}", requestURI, httpMethod);
            return false;
        }


        try {
            this.executeLogin(request, response);
        } catch (Exception e) {
            // 应用异常
            logger.info(e.getMessage());
            responseResult = Result.error(Result.Code.ACTION_FAILED, e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 请求失败拦截,请求终止，不进行转发直接返回客户端拦截结果
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception{
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setCharacterEncoding("UTF-8");

        httpServletResponse.getWriter().print(gson.toJson(responseResult));
        return false;
    }

    /**
     * 用户存在，执行登录认证
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        String token = JwtUtils.getRequestToken((HttpServletRequest)request);
        JwtToken jwtToken = new JwtToken(token);
        // 提交给AuthRealm进行登录认证
        getSubject(request, response).login(jwtToken);

        return true;
    }
}

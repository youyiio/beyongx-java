package com.beyongx.common.exception;

import com.beyongx.common.vo.Result;
import com.google.gson.Gson;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName NoPermissionException
 * @Description
 * @Author youyi.io
 * @Date 2020/5/30 15:12
 * @Version 1.0
 **/
@ControllerAdvice
public class NoPermissionException {

    /**
     * 用户角色权限认证 当没有权限时会报UnauthorizedException异常 此处处理异常给前端返回提示语
     * @param response
     * @param ex
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public void handleShiroException(HttpServletResponse response, Exception ex) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setCharacterEncoding("UTF-8");

        //Result result = Result.error(Result.Code.ACTION_FAILED, "访问了无权限目录");
        Result result = Result.error(Result.Code.ACTION_FAILED, ex.getMessage());

        Gson gson = new Gson();
        httpServletResponse.getWriter().print(gson.toJson(result));
    }

   //执行认证逻辑认证不通过时会走  过滤器AuthFilter中的onAccessDenied方法 所以这个异常不捕获
   /* @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public String AuthorizationException(Exception ex) {
        return "权限认证失败";
    }*/
}
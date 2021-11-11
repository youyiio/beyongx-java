package com.beyongx.common.aspect;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.beyongx.common.vo.Result;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 一般的参数绑定时候抛出的异常
     * @return
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public Result handleBindException(Exception e) {
        log.error("参数校验异常", e);

        String msg = "";

        /// BindException
        if (e instanceof BindException) {
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
//            FieldError fieldError = ((BindException) e).getFieldError();
//            if (fieldError != null) {
//                msg = fieldError.getDefaultMessage();
//            }
            List<String> defaultMsg = ((BindException) e).getBindingResult().getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            msg = defaultMsg.get(0);

            /// MethodArgumentNotValidException
        } else if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
            }
            /// ValidationException 的子类异常ConstraintViolationException
        } else if (e instanceof ConstraintViolationException) {
            /*
             * ConstraintViolationException的e.getMessage()形如
             *     {方法名}.{参数名}: {message}
             *  这里只需要取后面的message即可
             */
            msg = e.getMessage();
            if (msg != null) {
                int lastIndex = msg.lastIndexOf(':');
                if (lastIndex >= 0) {
                    msg = msg.substring(lastIndex + 1).trim();
                }
            }
            /// ValidationException 的其它子类异常
        } else {
            msg = "处理参数时异常";
        }


        return Result.error(Result.Code.E_PARAM_ERROR, msg);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public Result handleUnauthorizedException(Exception e) {
        log.error("访问的资源未授权", e);

        String msg = ((UnauthorizedException)e).getMessage();

        return Result.error(Result.Code.E_ACCESS_NOT_AUTH, "访问的资源未授权:" + msg);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(HttpServletResponse response, Exception e) {
        log.error("异常统一处理", e);

        if (e instanceof HttpStatusCodeException) {
            HttpStatusCodeException httpE = (HttpStatusCodeException)e;
            // 参数错误,返回错误数据
            //response.setStatus(HttpStatus.SC_BAD_REQUEST);
            // 返回错误信息
            return Result.error(httpE.getStatusCode().value(), httpE.getStatusText());
        }

        if (e instanceof UnknownHttpStatusCodeException) {
            UnknownHttpStatusCodeException httpE = (UnknownHttpStatusCodeException)e;
            // 返回错误信息
            return Result.error(Result.Code.E_UNKNOWN_ERROR, httpE.getStatusText());
        }

        //未知异常
        //response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        return Result.error(Result.Code.E_UNKNOWN_ERROR, e.getMessage() == null ? e.toString() : e.getMessage());
    }

}

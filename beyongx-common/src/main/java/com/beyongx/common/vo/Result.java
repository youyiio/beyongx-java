package com.beyongx.common.vo;

import lombok.Data;

@Data
public class Result {

    private int code;

    private String message;

    private Object data;

    public static final int SUCCESS = 1;
    public static Result success(Object data) {
        Result result = new Result();
        result.code = 1;
        result.message = "success";
        result.data = data;
        return result;
    }

    public static Result error(int code, String message) {
        Result result = new Result();
        result.code = code;
        result.message = message;
        result.data = null;
        return result;
    }

    public static Result result(int code, String message, Object data) {
        Result result = new Result();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }

    public static class Code {
        public static final int ACTION_SUCCESS             = 1;        //操作成功
        public static final int ACTION_FAILED              = 0;        //操作失败

        public static final int E_UNKNOWN_ERROR            = 2;        //未知异常
        public static final int E_PARAM_ERROR              = 3;        //参数错误
        public static final int E_PARAM_NULL               = 4;        //参数为空

        public static final int E_REQUEST_METHOD_UNSUPPORT  = 30001;   //请求方法不支持，请注意get,post,put,delete
        public static final int E_DATA_ACCESS               = 31000;  //数据库访问错误

        /**通用类错误**/
        public static final int E_VALIDATE_UNAUTHORIZED     = 40000;   //未授权的访问
        public static final int E_VALIDATE_UNSIGNED        = 40001;   //参数未进行签名
        public static final int E_VALIDATE_SIGN_UNMATCHED    = 40002;   //参数签名错误
        public static final int E_VALIDATE_TOKEN_INVALID   = 40003;   //token错误
        public static final int E_VALIDATE_MOBILE_INVALID  = 40004;   //手机号码格式不正确

        public static final int E_SMS_CODE_NOT_MATCH       = 41000;   //短信验证码错误
        public static final int E_SMS_CODE_EXPIRED         = 41001;   //短信验证码过期
        public static final int E_SMS_CODE_USED            = 41002;   //短信验证码已使用

        public static final int E_USER_ID_NOT_EXIST        = 42000;   //用户Id不存在
        public static final int E_USER_MOBILE_NOT_EXIST    = 42001;   //用户手机号不存在
        public static final int E_USER_PASSWORD_NOT_MATCH  = 42002;   //用户密码不正确
        public static final int E_USER_STATE_UNACTIVATED   = 42003;   //用户未激活
        public static final int E_USER_STATE_FROZEN        = 42004;   //用户已冻结
        public static final int E_USER_STATE_DELETED       = 42005;   //用户已删除

        public static final int E_COMMUNITY_ID_NOT_EXIST   = 43000;   //小区id不存在

        public static final int E_COMMUNITY_ID_IS_ENOUGH   = 43001;   //数据库已存在数据
    }
}

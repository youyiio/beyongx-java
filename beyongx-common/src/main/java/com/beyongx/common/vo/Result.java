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

        public static final int E_UNKNOWN_ERROR = 2;  //未知错误
        public static final int E_PARAM_ERROR	= 3;  //参数错误
        public static final int E_PARAM_EMPTY	= 4;  //参数为空
        public static final int E_PARAM_VALIDATE_ERROR = 5;  //参数验证错误
        public static final int E_TOKEN_EXPIRED	= 6;  //TOKEN不合法
        public static final int E_TOKEN_EMPTY	= 7;  //TOKEN参数缺失
        public static final int E_TOKEN_INVALID	= 8;  //TOKEN不合法
        public static final int E_ACCESS_NOT_AUTH	= 9;  //访问资源未授权
        public static final int E_ACCESS_NOT_FOUND = 10;  //访问资源未找到
        public static final int E_ACCESS_TIMEOUT  = 11;  //访问资源超时
        public static final int E_ACCESS_LIMIT = 12;  //访问受限
        public static final int E_DATA_NOT_FOUND = 13;  //数据为找到
        public static final int E_DATA_EXIST = 14;  //数据已存在
        public static final int E_DATA_ERROR = 15;  //数据错误
        public static final int E_DATA_VALIDATE_ERROR = 16;  //数据验证错误
        public static final int E_THIRDPARTY_ERROR = 17;  //第三方系统错误
        public static final int E_PARSE_JSON = 18;  //json解析错误
        public static final int E_PARSE_DATE = 19;  //date日期解析错误
        public static final int E_LOGIC_ERROR = 20;  //逻辑层操作错误
        public static final int E_MODEL_ERROR = 21;  //模型层操作错误
        public static final int E_DB_ERROR = 22;  //数据库操作错误
        public static final int E_CODE_INCORRECT = 23;  //验证码不正确
        public static final int E_CODE_EXPIRED = 24;  //验证码已过期
        public static final int E_CODE_USED = 25;  //验证码已使用
        public static final int E_RISK_ASSE_CODE_CHECK = 26;  //[风险评估]需要验证码检测
        public static final int E_RISK_ASSE_HUMAN_CHECK = 27;  //[风险评估]需要人类检测

        /** Http协议异常(保留与http状态码一致: 0-999) **/
        public static final int SC_BAD_REQUEST             = 400;       //错误请求，请检查请求地址及参数
        public static final int SC_UNAUTHORIZED            = 401;       //未通过身份验证(用户名或密码错误)
        public static final int SC_FORBIDDEN               = 403;       //服务器拒绝请求或非法访问
        public static final int SC_NOT_FOUND               = 404;       //请求资源未找到，请检查请求地址及参数
        public static final int SC_REQUEST_TIMEOUT         = 408;       //请求超时，请稍候重试
        public static final int SC_INTERNAL_SERVER_ERROR   = 500;       //服务器内部错误，无法完成请求，请稍候重试
        public static final int SC_BAD_GATEWAY             = 502;       //服务器宕机或正在升级
        public static final int SC_SERVICE_UNAVAILABLE     = 503;       //服务暂不可用，服务器过载或停机维护，请稍候重试

        /** 自定义异常类型代码 **/
        /** 错误码格式为X001,X为模块编号；**/
        /** 错误标识格式E_XXX_xxxxx,其中XXX为模块标识 **/
        //用户模块
        public static final int E_USER_NOT_EXIST               = 1001;     //用户不存在
        public static final int E_USER_MOBILE_NOT_EXIST        = 1002;     //手机号不存在
        public static final int E_USER_EMAIL_NOT_EXIST         = 1003;     //邮箱不存在
        public static final int E_USER_PASSWORD_INCORRECT      = 1004;     //密码不正确
        public static final int E_USER_MOBILE_HAS_EXIST        = 1005;     //手机号已经存在
        public static final int E_USER_EMAIL_HAS_EXIST         = 1006;    //邮箱已经存在
        public static final int E_USER_ACCOUNT_HAS_EXIST       = 1007;    //帐号已经存在
        public static final int E_USER_STATUS_NOT_ACTIVED      = 1008;     //用户未激活
        public static final int E_USER_STATUS_FREED            = 1009;     //用户已冻结
        public static final int E_USER_STATUS_DELETED          = 1010;     //用户已删除
        public static final int E_USER_STATUS_INVALID          = 1011;     //用户状态不合法

        //配置模块
        public static final int E_CONFIG_NOT_FOUND         = 1101;   //字典不存在;
        public static final int E_CONFIG_GROUP_KEY_UNIQ    = 1101;   //字典组和键的组合已被占用;

        //部门岗位模块
        public static final int E_DEPT_NOT_FOUND      = 1111;   //部门不存在;
        public static final int E_JOB_NOT_FOUND       = 1121;   //岗位不存在;

        //角色权限模块
        public static final int E_ROLE_NOT_FOUND       = 2001;   //角色不存在
        public static final int E_MENU_NOT_FOUND       = 2010;   //菜单不存在

        //内容管理模块
        public static final int E_CMS_ARTICLE_NOT_FOUND     = 3001;  //文章未找到
        public static final int E_CMS_CATEGORY_NOT_SUPPORT  = 3011;  //文章分类不支持
        public static final int E_CMS_LINK_NOT_FOUND        = 3021;  //友链不存在

    }

    public static class Msg {
        public static final String ACTION_SUCCESS             = "操作成功";
        public static final String ACTION_FAILED              = "操作失败";

        public static final String E_UNKNOWN_ERROR = "未知错误";
        public static final String E_PARAM_ERROR	= "参数错误";
        public static final String E_PARAM_EMPTY	= "参数为空";
        public static final String E_PARAM_VALIDATE_ERROR = "参数验证错误";
        public static final String E_TOKEN_EXPIRED	= "TOKEN已过期";
        public static final String E_TOKEN_EMPTY	= "TOKEN参数缺失";
        public static final String E_TOKEN_INVALID	= "TOKEN不合法";
        public static final String E_ACCESS_NOT_AUTH	= "访问资源未授权";
        public static final String E_ACCESS_NOT_FOUND = "访问资源未找到";
        public static final String E_ACCESS_TIMEOUT  = "访问资源超时";
        public static final String E_ACCESS_LIMIT = "访问受限";
        public static final String E_DATA_NOT_FOUND = "数据为找到";
        public static final String E_DATA_EXIST = "数据已存在";
        public static final String E_DATA_ERROR = "数据错误";
        public static final String E_DATA_VALIDATE_ERROR = "数据验证错误";
        public static final String E_THIRDPARTY_ERROR = "第三方系统错误";
        public static final String E_PARSE_JSON = "json解析错误";
        public static final String E_PARSE_DATE = "date日期解析错误";
        public static final String E_LOGIC_ERROR = "逻辑层操作错误";
        public static final String E_MODEL_ERROR = "模型层操作错误";
        public static final String E_DB_ERROR = "数据库操作错误";
        public static final String E_CODE_INCORRECT = "验证码不正确";
        public static final String E_CODE_EXPIRED = "验证码已过期";
        public static final String E_CODE_USED = "验证码已使用";
        public static final String E_RISK_ASSE_CODE_CHECK = "[风险评估]需要验证码检测";
        public static final String E_RISK_ASSE_HUMAN_CHECK = "[风险评估]需要人类检测";

        /** Http协议异常(保留与http状态码一致: 0-999) **/
        public static final String SC_BAD_REQUEST             = "错误请求，请检查请求地址及参数";
        public static final String SC_UNAUTHORIZED            = "未通过身份验证(用户名或密码错误)";
        public static final String SC_FORBIDDEN               = "服务器拒绝请求或非法访问";
        public static final String SC_NOT_FOUND               = "请求资源未找到，请检查请求地址及参数";
        public static final String SC_REQUEST_TIMEOUT         = "请求超时，请稍候重试";
        public static final String SC_INTERNAL_SERVER_ERROR   = "服务器内部错误，无法完成请求，请稍候重试";
        public static final String SC_BAD_GATEWAY             = "服务器宕机或正在升级";
        public static final String SC_SERVICE_UNAVAILABLE     = "服务暂不可用，服务器过载或停机维护，请稍候重试";

        /** 自定义异常类型代码 **/
        /** 错误码格式为X001,X为模块编号；**/
        /** 错误标识格式E_XXX_xxxxx,其中XXX为模块标识 **/
        //用户模块
        public static final String E_USER_NOT_EXIST               = "用户不存在";
        public static final String E_USER_MOBILE_NOT_EXIST        = "手机号不存在";
        public static final String E_USER_EMAIL_NOT_EXIST         = "邮箱不存在";
        public static final String E_USER_PASSWORD_INCORRECT      = "密码不正确";
        public static final String E_USER_MOBILE_HAS_EXIST        = "手机号已经存在";
        public static final String E_USER_EMAIL_HAS_EXIST         = "邮箱已经存在";
        public static final String E_USER_ACCOUNT_HAS_EXIST       = "帐号已经存在";
        public static final String E_USER_STATUS_NOT_ACTIVED      = "用户未激活";
        public static final String E_USER_STATUS_FREED            = "用户已冻结";
        public static final String E_USER_STATUS_DELETED          = "用户已删除";
        public static final String E_USER_STATUS_INVALID          = "用户状态不合法";

        //配置模块
        public static final String E_CONFIG_NOT_FOUND         = "字典不存在";
        public static final String E_CONFIG_GROUP_KEY_UNIQ    = "字典组和键的组合已被占用";

        //部门岗位模块
        public static final String E_DEPT_NOT_FOUND      = "部门不存在";
        public static final String E_JOB_NOT_FOUND       = "岗位不存在";

        //角色权限模块
        public static final String E_ROLE_NOT_FOUND       = "角色不存在";
        public static final String E_MENU_NOT_FOUND       = "菜单不存在";

        //内容管理模块
        public static final String E_CMS_ARTICLE_NOT_FOUND     = "文章未找到";
        public static final String E_CMS_CATEGORY_NOT_SUPPORT  = "文章分类不支持";
        public static final String E_CMS_LINK_NOT_FOUND        = "友链未找到";
    }
}

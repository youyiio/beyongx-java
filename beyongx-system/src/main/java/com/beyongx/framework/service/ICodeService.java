package com.beyongx.framework.service;

public interface ICodeService {
    
    public static final int STATUS_UNUSED = 1; //未使用
    public static final int STATUS_USED = 2; //已使用

    public static final String TYPE_REGISTER = "register";
    public static final String TYPE_LOGIN = "login";
    public static final String TYPE_RESET_PASSWORD = "reset_password";
    
    /**
     * 发送短信验证码，条件：1、配置短信通道；
     * @param mobile
     * @param type
     * @return
     */
    boolean sendCodeByMobile(String mobile, String type);

    /**
     * 发送邮件验证码，条件：1、配置邮件通道；
     * @param mobile
     * @param type
     * @return
     */
    boolean sendCodeByEmail(String mobile, String type);

    /**
     * 校对验证码
     * @param type
     * @param username
     * @param code
     * @return
     */
    boolean checkCode(String type, String username, String code);

    /**
     * 消费验证码
     * @param type
     * @param username
     * @param code
     * @return
     */
    boolean consumeCode(String type, String username, String code);
}

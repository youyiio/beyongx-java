package com.beyongx.framework.service;

public interface IRiskAsseService {
    
    /**
     * 登录风险评估
     * @param username
     * @param ip
     * @param code
     * @return
     */
    boolean checkLogin(String username, String ip, String code);

    boolean resetLogin(String username, String ip);

    /**
     * 短信风险评估
     * @param username
     * @param ip
     * @return
     */
    boolean checkSms(String mobile, String ip);

    boolean resetSms(String mobile, String ip);
}

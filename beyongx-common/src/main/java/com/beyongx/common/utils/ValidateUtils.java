package com.beyongx.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {

	// 通行证正则表达式
    private static final Pattern PATTERN_USERNAME = Pattern.compile("[a-zA-Z0-9_]{4,32}");
    private static final Pattern PATTERN_PASSWORD = Pattern.compile("[a-zA-Z0-9_]{6,16}");
    private static final Pattern PATTERN_EMAIL = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    );
    private static final Pattern PATTERN_MOBILE = Pattern.compile("1[0-9]{10}");
    
	public static boolean isValidUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		Matcher m = PATTERN_USERNAME.matcher(username);
	    return m.matches();
	}
	
	public static boolean isValidPassword(String password) {
		if (StringUtils.isEmpty(password)) {
			return false;
		}
		Matcher m = PATTERN_PASSWORD.matcher(password);
		return m.matches();
	}
	
	public static boolean isValidEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		Matcher m = PATTERN_EMAIL.matcher(email);
		return m.matches();
	}
	
	public static boolean isValidMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return false;
		}
		Matcher m = PATTERN_MOBILE.matcher(mobile);
		return m.matches();
	}
	
	   //字符是数字判断
		public static boolean isNumber(String str) {
			if (str == null) {
				return false;
			}
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(str);
			if (!isNum.matches()) {
				return false;
			}
			return true;
		}
		
		//字符是数值判断
		public static boolean isNumeric(String str) {
			if (str == null) {
				return false;
			}
			Pattern pattern = Pattern.compile("[0-9.]*");
			Matcher isNum = pattern.matcher(str);
			if (!isNum.matches()) {
				return false;
			}
			return true;
		}
}

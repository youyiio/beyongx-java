package com.beyongx.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

public class ParameterUtils {

    //从请求中获取String类型的参数
    public static String getString(HttpServletRequest request, String name) {
        return getString(request, name, null);
    }

    public static String getString(HttpServletRequest request, String name, String defaultValue) {
        String param = request.getParameter(name);
        if (param == null || param.trim().length() == 0) {
            return defaultValue;
        }
        return param.trim();
    }

    public static Integer getInteger(HttpServletRequest request, String name) {
        return  getInteger(request, name, null);
    }

    public static Integer getInteger(HttpServletRequest request, String name, Integer defaultValue) {
        String param = getString(request, name);
        if (param == null) {//没传参数
            return defaultValue;
        }
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {//如果参数不是数字格式
            e.printStackTrace();
        }
        return null;
    }

    public static int getInt(HttpServletRequest request, String name) {
        Integer i = getInteger(request, name);
        return i == null ? 0 : i;
    }

    public static Double getDouble(HttpServletRequest request, String name) {
        String param = getString(request, name);
        if (param == null) {//没传参数
            return null;
        }
        try {
            return Double.parseDouble(param);
        } catch (NumberFormatException e) {//如果参数不是数字格式
            e.printStackTrace();
        }
        return null;
    }

    // 在Servlet中调用getDate方法时，根据输入格式选择下面几种参数
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static Date getDate(HttpServletRequest request, String name, SimpleDateFormat format) {
        String param = getString(request, name);
        if (param == null) {
            return null;
        }

        try {
            return format.parse(param);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取参数串 &拼接
    public static String getParams(HttpServletRequest request) {
        StringBuffer paramsBuffer = new StringBuffer("");
        Enumeration<String> paramNames = request.getParameterNames();
        String paramName;

        while (paramNames.hasMoreElements()) {
            paramName = paramNames.nextElement();
            if (paramName.toUpperCase(Locale.ENGLISH).contains("PASSWORD")) {
                continue;
            }
            paramsBuffer.append(paramName).append("=");

            String[] values = request.getParameterValues(paramName);

            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    if (i > 0) {
                        paramsBuffer.append(",");
                    }
                    paramsBuffer.append(values[i]);
                }
            }
            paramsBuffer.append("&");
        }

        return paramsBuffer.toString();
    }
}



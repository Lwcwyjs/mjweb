package com.commons.sql;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 防止SQL注入的拦截器
 *
 * @author tyee.noprom@qq.com
 * @time 2/13/16 8:22 PM.
 */
public class SqlInjectInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        Enumeration<String> names = request.getParameterNames();
        boolean x=true;
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name).toLowerCase();
            String  valuex = clearXss(value);
            if(valuex.equals(value))
            {
            	x=true;
            }
            else 
            {
            	x=false;
            	break;
            }

        }
        return x;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }

    /**
     * 处理字符转义
     *
     * @param value
     * @return
     */
    private String clearXss(String value) {
        if (value == null || "".equals(value)) {
            return value;
        }
        value = value.replace("'", "");
        value = value.replace(";", "");
        value = value.replace("script", "");
        value = value.replace("drop ", "");
        value = value.replace(" or ", "");
        value = value.replace("exec ", "");
        value = value.replace("insert ", "");
        value = value.replace("execute ", "");
        value = value.replace("select ", "");
        value = value.replace("delete ", "");
        value = value.replace("update ", "");
        value = value.replace("master", "");
        value = value.replace("javascript ", "");
        value = value.replace("count(*)", "");
        value = value.replace("truncate ", "");
        value = value.replace("declare", "");
        value = value.replace("union ", "");
        value = value.replace("join ", "");
        value = value.replace(" create ", "");//因为有字段名为createdate的。
        value = value.replace("grant", "");
        value = value.replace("iframe", "");
        value = value.replace("dbms_", "");//oracle的关键字
        value = value.replace("iframe", "");
        value = value.replace("sleep(", "");
        value = value.replace(" and ", "");
        value = value.replace("--", "");
        return value;
    }
}
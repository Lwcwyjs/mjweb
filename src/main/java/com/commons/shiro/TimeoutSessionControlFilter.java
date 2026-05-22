package com.commons.shiro;

import java.util.Deque;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

public class TimeoutSessionControlFilter  extends AccessControlFilter  { 
	private String timeoutUrl; //踢出后到的地址 
    private String kickoutUrl; //踢出后到的地址 
    private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户 
    private int maxSession = 1; //同一个帐号最大会话数 默认1 
 
    private SessionManager sessionManager; 
    private Cache<String, Deque<Session>> cache; 
 
    public void setKickoutUrl(String kickoutUrl) { 
        this.kickoutUrl = kickoutUrl; 
    } 
    public void setTimeoutUrl(String timeoutUrl) { 
        this.timeoutUrl = timeoutUrl; 
    } 
    public void setKickoutAfter(boolean kickoutAfter) { 
        this.kickoutAfter = kickoutAfter; 
    } 
 
    public void setMaxSession(int maxSession) { 
        this.maxSession = maxSession; 
    } 
 
    public void setSessionManager(SessionManager sessionManager) { 
        this.sessionManager = sessionManager; 
    } 
 
    public void setCacheManager(CacheManager cacheManager) { 
        this.cache = cacheManager.getCache("shiro-kickout-session"); 
    } 
 
    @Override 
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception { 
       String url = ((HttpServletRequest) request).getRequestURL().toString();
       if(url.contains("/admin/login")){
    	   return Boolean.FALSE;
       }
       if(isLoginRequest(request, response)){
    		 return Boolean.TRUE;
    	}
    	 ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
         String username=user.getName();
         cache.remove(username);
    	 if(ShiroFilterUtils.isAjax(request)){
             ShiroFilterUtils.out(response);

             //通过返回TRUE，通过前台的统一AJAX接受头部设置的sessionstatus参数，判断是否跳转到登录页面

             return Boolean.FALSE;
         }//FALSE  Session失效，切实非AJAX请求，验证是否，调用onAccessDenied跳转到登录页面
         
    	 WebUtils.issueRedirect(request, response, timeoutUrl);
    	 return Boolean.FALSE;

    	

    } 
 
    @Override 
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception { 

        return true; 
    } 
}

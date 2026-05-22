package com.commons.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache; 
import org.apache.shiro.cache.CacheManager; 
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject; 
import org.apache.shiro.web.filter.AccessControlFilter; 
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.base.PublicService;

import javax.servlet.ServletRequest; 
import javax.servlet.ServletResponse; 
import java.io.Serializable; 
import java.util.Deque; 
import java.util.LinkedList; 
 
/** 
 * <p>User: Zhang Kaitao 
 * <p>Date: 14-2-18 
 * <p>Version: 1.0 
 */ 
public class KickoutSessionControlFilter extends AccessControlFilter { 
	private String timeoutUrl; //踢出后到的地址 
	@Autowired
	private PublicService publicService;
	
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

    	 return Boolean.FALSE;

    	

    } 
 
    @Override 
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception { 
        Subject subject = getSubject(request, response); 
        if(!subject.isAuthenticated() && !subject.isRemembered()) { 
            //如果没有登录，直接进行之后的流程 
            return true; 
        } 
        Session session = subject.getSession(); 
 /*       String username = (String)  SecurityUtils.getSubject().getPrincipal(); */
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String username=user.getName();
        Serializable sessionId = session.getId(); 
 
        //TODO 同步控制 
        Deque<Session> deque = cache.get(username); 
        if (deque == null) { 
            deque = new LinkedList<Session>(); 
            cache.put(username, deque); 
        } 
    	Session tempSession = null;
        if (deque.size()>0)
        {
        	tempSession=deque.getFirst();
        }
        //如果队列里没有此session，且用户没有被踢出；放入队列 
        if (null==tempSession){//说明deque里面没有值。继续判断
        	if (!deque.contains(session) && session.getAttribute("kickout") == null) { 
                deque.push(session); 
            } 
        } else{
        	
        	if(tempSession.getId().equals(session.getId()))
        	{
        		
        	}
        	else if (session.getAttribute("kickout") == null) {
                deque.push(session); 
            } 
        }
        //如果队列里的session数超出最大会话数，开始踢人 
        while(deque.size() > maxSession) { 
            //Serializable kickoutSessionId = null; 
        	Session kickoutSession=null;
            if(kickoutAfter) { //如果踢出后者 
                kickoutSession = deque.removeFirst(); 
            } else { //否则踢出前者 
                kickoutSession = deque.removeLast(); 
            } 
            try { 
                //Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId)); 
                if(kickoutSession != null) { 
                    //设置会话的kickout属性表示踢出了 
                    kickoutSession.setAttribute("kickout", true); 
                } 
            } catch (Exception e) {//ignore exception 
                System.out.println(e);
            } 
        } 
 
        //如果被踢出了，直接退出，重定向到踢出后的地址 
        if (session.getAttribute("kickout") != null) { 
            //会话被踢出了 
            try { 
                subject.logout(); 
            } catch (Exception e) { //ignore 
            } 
            saveRequest(request); 
            publicService.insertLogInfo("多地登录被顶掉:"+username, "登陆", "0", (long) 0, username,
					"","其他");
            WebUtils.issueRedirect(request, response, kickoutUrl);
            return false; 
        } 
 
        return true; 
    } 
} 

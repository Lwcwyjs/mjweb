package com.commons.shiro;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

/**
 * 
 * @author songdh
 *
 */
public class MySessionManager extends DefaultWebSessionManager{
	
	 public boolean isServletContainerSessions() {
	        return true;
	 }
}

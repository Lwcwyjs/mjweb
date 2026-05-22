package com.commons.scan.aspectService.inter;

import org.aspectj.lang.ProceedingJoinPoint;

import com.service.base.PublicService;
/**
 * @description：切面服务
 * @author songdh
 * @data 2018/11/07 09:20
 *
 */
public interface aspectService {
//	public 
	
	/**
	 * @description：切面记录操作日志
	 * @author：donghe.song
	 * @date: 2018/11/13 09:10
	 */
	public void aspectLogInfo(ProceedingJoinPoint pjp,PublicService publicService);
	
	
}

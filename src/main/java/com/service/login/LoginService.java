package com.service.login;

/**
 * @description：登录时验证类
 * @author：donghe.song
 * @date: 2018/06/13 16:10
 */

import com.commons.result.Result;
import com.model.sysmanage.SysUser;

public interface LoginService {
	/**
	 * @description：登录时验证用户登录状态
	 * @author：donghe.song
	 * @date: 2018/06/13 16:10
	 */
	public Result chekUserState(SysUser sysuser);
	
	/**
	 * @description：登录时验证有效期
	 * @author：lwc
	 * @date: 2019/03/19 16:28
	 */
	public Result checkValid(SysUser sysuser);

	/**
	 * @description：登录时验证用户登录IP黑名单
	 * @author：donghe.song
	 * @date: 2018/06/13 16:10
	 */
	public Result checkBlackIP(String ip);
	/**
	 * @description：登录时验证用户数据安全
	 * @author：donghe.song
	 * @date: 2018/06/13 16:10
	 */
	
	
}

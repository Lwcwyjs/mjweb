package com.controller.sysmanage;

import com.commons.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @description：用户申请注册管理
 * @author：wangwj
 * @date：2016/5/27  14:51
 */
@Controller
@RequestMapping("/front/register")
public class UserRegisterController extends BaseController {
	@RequestMapping(value="/code/{randid}",method = RequestMethod.GET)
	public String getVerifyCode(@PathVariable String randid)
	{
		return "login/image";
	}
	
}

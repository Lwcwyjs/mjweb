package com.controller.login;

import com.commons.base.BaseController;
import com.service.configmanage.SysOptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {

	@Autowired
	private SysOptionService optionService;
	
	@RequestMapping(value = "/")
	public String homePage(HttpServletRequest request) {
		String title=optionService.getConfigValue("服务配置", "系统标题");
		String glbm=optionService.getConfigValue("服务配置", "管理部门");
		String lxdh=optionService.getConfigValue("服务配置", "联系电话");
		title=StringUtils.isBlank(title)?"机动车检验智能审核系统":title;
		glbm=StringUtils.isBlank(glbm)?"管理中心":glbm;
		lxdh=StringUtils.isBlank(lxdh)?"400-7777-266":lxdh;
		request.setAttribute("title", title);
		request.setAttribute("glbm", glbm);
		request.setAttribute("lxdh", lxdh);
		
		return "login/login";
	}

}

package com.service.configmanage;

import com.service.sysmanage.SysOrganizationService;
import com.service.base.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class  ConfigServiceImp implements ConfigService{
	static HashMap<String, Object> configobj;
	@Autowired
	private PublicService publicService;

	@Autowired
	private SysOrganizationService sysOrganizationService;

	@Autowired
	private SysOptionsService sysOptionsService;

	public String setvalue() {
		HashMap<String, Object> configobjex=new HashMap<String,Object>();
		configobjex.put("organ", sysOrganizationService.findAllOrgan());
		configobjex.put("zpzl",sysOptionsService.findOptionList("照片参数"));
		configobjex.put("bdcs",sysOptionsService.findOptionList("比对参数"));
		configobjex.put("cbcs",sysOptionsService.findOptionList("车标参数"));
		configobjex.put("fwpz",sysOptionsService.findOptionList("服务配置"));
		configobjex.put("rwcs",sysOptionsService.findOptionList("任务配置参数"));
		configobj=configobjex;
		return "1";
	}

	@Override
	public HashMap<String, Object> getvalue() {
		if (configobj==null){
			setvalue();
		}
		return configobj;
	}
}

package com.service.configmanage;

import com.commons.sql.SQLBean;
import com.model.configmange.SysCode;
import com.model.configmange.SysOptions;
import com.service.base.PublicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysOptionServiceImpl implements SysOptionService {
	
	@Autowired
	private PublicService publicService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String getConfigValue(String configType, String configItem) {
		logger.info("getConfigValue："+configType+"-"+configItem);
		String sql;
		String ret="";
		SysOptions baseOption=new SysOptions();
		baseOption.setOption_kind(configType);
		baseOption.setOption_des(configItem);
		SQLBean sqlBean=new SQLBean("AI_SYS_OPTIONS","","option_kind,option_des");
		sql=sqlBean.generateSelectSQL(baseOption);
		logger.info(sql);
		baseOption=publicService.selectObj(sql, SysOptions.class);
		if (baseOption!=null){
			ret=baseOption.getOption_params();
		}
		return ret;
	}
	
	@Override
	public String getConfigstop(String configType, String configItem) {
		String sql;
		String ret="";
		SysOptions baseOption=new SysOptions();
		baseOption.setOption_kind(configType);
		baseOption.setOption_des(configItem);
		SQLBean sqlBean=new SQLBean("AI_SYS_OPTIONS","","option_kind,option_des");
		sql=sqlBean.generateSelectSQL(baseOption);
		baseOption=publicService.selectObj(sql, SysOptions.class);
		if (baseOption!=null){
			ret=baseOption.getStatus();
		}
		return ret;
	}
	
	@Override
	public String getSysValue(String oiName, String oiCode) {
		String sql;
		String ret="";
        SysCode sysCode =new SysCode();
        sysCode.setOi_name(oiName);
        sysCode.setOi_code(oiCode);
		SQLBean sqlBean=new SQLBean("ai_sys_code","","oi_name,oi_code");
		sql=sqlBean.generateSelectSQL(sysCode);
		sysCode =publicService.selectObj(sql, SysCode.class);
		if (sysCode !=null){
			ret= sysCode.getOi_value();
		}
		return ret;
	}
	
	@Override
	public String getSysCode(String oiName, String oiValue) {
		String sql;
		String ret="";
        SysCode sysCode =new SysCode();
        sysCode.setOi_name(oiName);
        sysCode.setOi_value(oiValue);
		SQLBean sqlBean=new SQLBean("ai_sys_code","","oi_name,oi_value");
		sql=sqlBean.generateSelectSQL(sysCode);
		sysCode =publicService.selectObj(sql, SysCode.class);
		if (sysCode !=null){
			ret= sysCode.getOi_code();
		}
		return ret;
	}


}

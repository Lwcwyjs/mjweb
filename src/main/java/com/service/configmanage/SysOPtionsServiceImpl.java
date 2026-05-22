package com.service.configmanage;

import java.util.List;
import javax.annotation.Resource;

import com.model.configmange.SysOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.service.base.PublicService;

@Service
public class SysOPtionsServiceImpl implements SysOptionsService {
	@Autowired
	@Resource
	private PublicService publicService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
     * 获取系统配置value值
     * @author lvwc
     * @param option_kind 配置类型 
     * @param option_des 配置描述
     * @return 配置值
     * */
	@Override
	public String findOptionValue(String option_kind,String option_des){
		logger.info("findOptionValue："+option_kind+"-"+option_des);
		System.out.println("findOptionValue："+option_kind+"-"+option_des);
		SysOptions sysOptions =new SysOptions();
		sysOptions.setOption_kind(option_kind);
		sysOptions.setOption_des(option_des);
		SysOptions sysOptionsex =publicService.selectObj("AI_SYS_OPTIONS", "", "option_kind,option_des","option_value","","", sysOptions);
		if (sysOptionsex !=null)
		{
			return sysOptionsex.getOption_value();
		}
		else
		{
			return "";
		}
	}
	/**
     * 获取系统配置列表
     * @author lvwc
     * @param option_kind 配置类型 
     * @return 配置列表
     * */
	@Override
	public List<SysOptions> findOptionList(String option_kind){
		SysOptions sysOptions =new SysOptions();
		sysOptions.setOption_kind(option_kind);
		List<SysOptions> sysOptionslist =publicService.selectObjs("AI_SYS_OPTIONS", "", "option_kind,option_des,is_stop","option_value","","", sysOptions);
		return sysOptionslist;
	}
}

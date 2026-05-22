package com.service.configmanage;

public interface SysOptionService {
	
	public String getConfigValue(String configType,String configItem);

	String getSysCode(String oiName, String oiValue);

	String getSysValue(String oiName, String oiCode);
	
	String getConfigstop(String configType, String configItem) ;

}

package com.service.configmanage;

import java.util.List;

import com.model.configmange.SysOptions;

public interface SysOptionsService {
    /**
     * 获取系统配置value值
     * @author lvwc
     * @param option_kind 配置类型 
     * @param option_des 配置描述
     * @return 配置值
     * */
    String findOptionValue(String option_kind,String option_des);
    
    /**
     * 获取系统配置列表
     * @author lvwc
     * @param option_kind 配置类型 
     * @return 配置列表
     * */
    List<SysOptions> findOptionList(String option_kind);
}

package com.service.logmanage;

import com.model.logmange.SysLogInfo;

import java.util.Map;


/**
 * @description：操作日志查询
 * @author：JiaoSiYuan
 * @date：2015/10/1 14:51
 */
public interface SysLogInfoService {
    

    /**
     * 根据numid查询日志信息
     *
     * @param id
     * @return
     */
	SysLogInfo findLogInfoById(Long id);

    
    Map<String,Object> findBaseData(String param);

}

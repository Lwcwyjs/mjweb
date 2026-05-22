package com.service.logmanage;

import com.model.sysmanage.SysOrganization;
import com.model.sysmanage.SysRole;
import com.model.sysmanage.SysUserRole;
import com.model.configmange.SysOptions;
import com.model.configmange.SysCode;
import com.service.base.PublicService;
import com.model.logmange.SysLogInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SysLogInfoServiceImpl implements SysLogInfoService {


	@Autowired
	private PublicService publicService;



	@Override
	public SysLogInfo findLogInfoById(Long num_id) {
		SysLogInfo loginfo = (SysLogInfo)publicService.selectObject("SELECT num_id,user_id,organization,user_name,terminal_id,operate_type,operate_time,operate_condition,operate_result,operate_module,terminal_type,jyw FROM AI_SYS_LOG_INFO WHERE num_id='"+num_id+"'", SysLogInfo.class);
		return loginfo;
	}


	@Override
	public Map<String, Object> findBaseData(String param) {
		Map<String, Object> baseData = new HashMap<String, Object>();

		switch (param.toUpperCase()) {
		case "ORGAN":
			List<SysOrganization> organs = publicService.selectObjs("ai_sys_organization", "", "", new SysOrganization());
			for (SysOrganization organ : organs) {
				baseData.put(organ.getOrgan(), organ.getName());
			}
			break;
		case "CYQXH":
			List<SysOrganization> cyqxhs = publicService.selectObjs("ai_sys_organization", "", "", new SysOrganization());
			for (SysOrganization cyqxh : cyqxhs) {
				baseData.put(cyqxh.getOrgan(), cyqxh.getName());
			}
			break;
		case "ORGANNAME":
			List<SysOrganization> organsName = publicService.selectObjs("ai_sys_organization", "", "",
					new SysOrganization());
			for (SysOrganization organ : organsName) {
				baseData.put(organ.getOrgan(), organ.getOrgan() + "\t" + organ.getName());
			}
			break;

		case "ROLE":
			List<SysRole> sysRoles = publicService.selectObjs("ai_sys_role", "", "", new SysRole());
			for (SysRole sysRole : sysRoles) {
				baseData.put(sysRole.getId().toString(), sysRole.getName());
			}
			break;
		case "BASEOPTIONS":
			List<SysOptions> baseoptions = publicService.selectObjs("AI_SYS_OPTIONS", "", "", new SysOptions());
			for (SysOptions bp : baseoptions) {
				Map<String, Object> sub = new HashMap<String, Object>();
				if (baseData.containsKey(bp.getOption_kind())) {
					sub = (Map<String, Object>) baseData.get(bp.getOption_kind());
					baseData.remove(bp.getOption_kind());
				}
				if (!sub.containsKey(bp.getOption_value())) {
					sub.put(bp.getOption_value(), bp.getOption_des());
				}
				baseData.put(bp.getOption_kind(), sub);
			}
			break;
		case "USERROLE":
			Map<String, Object> r = new HashMap<String, Object>();
			List<SysUserRole> sysUserRoles = publicService.selectObjs("ai_sys_user_role", "", "", new SysUserRole());
			List<SysRole> roles2 = publicService.selectObjs("ai_sys_role", "", "", new SysRole());
			for (SysRole sysRole : roles2) {
				if (StringUtils.isNoneBlank(sysRole.getId().toString()) && StringUtils.isNoneBlank(sysRole.getName()))
					r.put(sysRole.getId().toString(), sysRole.getName());
			}
			for (SysUserRole sysUserRole : sysUserRoles) {
				if (sysUserRole.getUser_id() != null && sysUserRole.getRole_id() != null) {
					String strKey = sysUserRole.getUser_id().toString();
					String strValue = r.containsKey(sysUserRole.getRole_id().toString())
							? r.get(sysUserRole.getRole_id().toString()).toString() : "";

					if (baseData.containsKey(strKey)) {
						strValue = baseData.get(strKey) + "," + strValue;
						baseData.remove(strKey);
					}
					baseData.put(strKey, strValue);
				}
			}
			break;
		case "SYSCODE":
			List<SysCode> sysCodes = publicService.selectObjs("ai_sys_code", "", "", new SysCode());
			for (SysCode scode : sysCodes) {
				Map<String, Object> sub = new HashMap<String, Object>();
				if (baseData.containsKey(scode.getOi_name())) {
					sub = (Map<String, Object>) baseData.get(scode.getOi_name());
					baseData.remove(scode.getOi_name());
				}
				if (!sub.containsKey(scode.getOi_code())) {
					sub.put(scode.getOi_code(), scode.getOi_value());
				}
				baseData.put(scode.getOi_name(), sub);
			}
			break;
		}
		return baseData;
	}

}

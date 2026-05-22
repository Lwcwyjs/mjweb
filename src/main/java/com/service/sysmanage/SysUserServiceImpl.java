package com.service.sysmanage;

import com.commons.publicTool.PublicMethordUtil;
import com.model.configmange.SysCode;
import com.model.configmange.SysOptions;
import com.model.securitymange.SysTerminalInfo;
import com.model.sysmanage.SysOrganization;
import com.model.sysmanage.SysRole;
import com.model.sysmanage.SysUser;
import com.model.sysmanage.SysUserRole;
import com.service.base.PublicService;
import com.service.base.PublicServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {

	private static Logger LOGGER = LoggerFactory.getLogger(SysUserServiceImpl.class);

	@Autowired
	private PublicService publicService;

	@Override
	public SysUser findUserByLoginName(String loginname) {
		SysUser sysUser = new SysUser();
		sysUser.setLoginname(loginname);
		
		return publicService.selectObj("ai_sys_user", "CREATEDATEEND,ROLE_ID,mmdqdays,yhdqdays", "loginname", "loginname,name,organ", "", "", sysUser);
		//.selectObj("ai_sys_user", "", "loginname", sysUser);
	}
	
    /** 
     * SpringMvc下获取request 
     *  
     * @return 
     */  
    public  HttpServletRequest getRequest() {  
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();  
        return request;  
    } 
    /**
     * 根据当前IP获取此IP记录
     */
	@Override
	public SysTerminalInfo findTerminalByIp() {
		HttpServletRequest request = getRequest();
		PublicServiceImpl publics = new PublicServiceImpl();
		String ip = publics.getRemoteHost(request);
		SysTerminalInfo sysTerminalInfo = new SysTerminalInfo();
		sysTerminalInfo.setTerminal_id(ip);
		sysTerminalInfo = publicService.selectObj("ai_sys_TERMINALINFO", "", "terminal_id", sysTerminalInfo);
		if (sysTerminalInfo == null) {
			sysTerminalInfo = new SysTerminalInfo();
			sysTerminalInfo.setTerminal_id(ip);
			return sysTerminalInfo;
		}
		return sysTerminalInfo;
	}

	@Override
	public SysUser findUserById(Long id) {
		SysUser sysUser = new SysUser();
		sysUser.setId(id);
		sysUser = publicService.selectObj("ai_sys_user", "CREATEDATEEND,ROLE_ID,mmdqdays,yhdqdays", "id", "loginname,name,organ", "", "", sysUser);
        if(sysUser!=null) {
			SysUserRole ur = new SysUserRole();
			ur.setUser_id(sysUser.getId());
			ur = publicService.selectObj("ai_sys_user_role", "", "user_id", ur);

			sysUser.setRole_id(ur.getRole_id());
		}

		return sysUser;
	}

	@Override
	public void addUser(SysUser sysUser) {
		//校验位
		sysUser.setJyw(PublicMethordUtil.md5(sysUser.getLoginname()+sysUser.getName()+sysUser.getStatus()));
		// 保存角色
		Long role_id = sysUser.getRole_id();
		// 新建用户
		publicService.insert("ai_sys_user", "id,createdateend,mmdqdays,yhdqdays", "id", "loginname,name,organ", sysUser);
		// 查询新建用户
		//sysUser = publicService.selectObj("ai_sys_user", "", "loginname", sysUser);		
		SysUser sUser=findUserByLoginName(sysUser.getLoginname());

		// 新建用户角色关联
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setUser_id(sUser.getId());
		sysUserRole.setRole_id(role_id);
		publicService.delete("ai_sys_user_role", "", "user_id", sysUserRole);
		publicService.insert("ai_sys_user_role", "id", "id", sysUserRole);
	}

	@Override
	public void updateUser(SysUser sysUser) {
		//校验位
		sysUser.setJyw(PublicMethordUtil.md5(sysUser.getLoginname()+sysUser.getName()+sysUser.getStatus()));
				
		// 保存角色
		Long role_id = sysUser.getRole_id();
		// 修改用户
		publicService.update("ai_sys_user", "id,createdateend,loginname,mmdqdays,yhdqdays", "id","loginname,name,organ","", sysUser);

		// 删除用户角色关联
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setUser_id(sysUser.getId());
		sysUserRole.setRole_id(role_id);
		publicService.delete("ai_sys_user_role", "", "user_id", sysUserRole);
		publicService.insert("ai_sys_user_role", "id", "id", sysUserRole);
	}
	@Override
	public void updateUserex(SysUser sysUser) {
		//校验位
		sysUser.setJyw(PublicMethordUtil.md5(sysUser.getLoginname()+sysUser.getName()+sysUser.getStatus()));
				
		// 保存角色
		Long role_id = sysUser.getRole_id();
		// 修改用户
		publicService.update("ai_sys_user", "id,createdateend,role_id,loginname,mmdqdays,yhdqdays", "id","loginname,name,organ","", sysUser);

	}

	@Override
	public void deleteUserById(Long id) {
		// 删除用户
		SysUser sysUser = new SysUser();
		sysUser.setId(id);
		publicService.delete("ai_sys_user", "", "id", sysUser);

		// 删除用户角色关联
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setUser_id(id);
		publicService.delete("ai_sys_user_role", "", "user_id", sysUserRole);
	}

	@Override
	public Map<String, Object> findBaseData(String param) {
		Map<String, Object> baseData = new HashMap<String, Object>();

		switch (param.toUpperCase()) {
		case "CYQXH":
			List<SysOrganization> cyqxhs = publicService.selectObjs("ai_sys_organization", "", "", new SysOrganization());
			for (SysOrganization cyqxh : cyqxhs) {
				baseData.put(cyqxh.getOrgan(), cyqxh.getName());
			}
			break;
		case "ORGAN":
			List<SysOrganization> organs = publicService.selectObjs("ai_sys_organization", "", "", new SysOrganization());
			for (SysOrganization organ : organs) {
				baseData.put(organ.getOrgan(), organ.getJc());
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
			List<SysOptions> baseoptions = publicService.selectObjs("select option_kind,option_des,option_value from AI_SYS_OPTIONS", SysOptions.class);
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
			List<SysCode> sysCodes = publicService.selectObjs("ai_sys_code", "mmdqdays,yhdqdays", "", new SysCode());
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

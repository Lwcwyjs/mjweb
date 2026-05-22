package com.model.login;

import com.model.sysmanage.SysOrganization;
import com.model.sysmanage.SysUser;

public class CurrentUserInfo {
	SysOrganization organization;
	SysUser sysUser;
	public SysOrganization getOrganization() {
		return organization;
	}
	public void setOrganization(SysOrganization organization) {
		this.organization = organization;
	}
	public SysUser getUser() {
		return sysUser;
	}
	public void setUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	
	
}

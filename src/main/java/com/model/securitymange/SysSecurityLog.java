package com.model.securitymange;


public class SysSecurityLog {
	private Long num_id;
	private String logtype;
	private String organization;
	private String user_name;
	private String terminal_id;
	private String operate_time;
	private String operate_content;
	private String jyw;

	public Long getNum_id() {
		return num_id;
	}

	public void setNum_id(Long num_id) {
		this.num_id = num_id;
	}

	public String getLogtype() {
		return logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	public String getOperate_time() {
		return operate_time;
	}

	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}

	public String getOperate_content() {
		return operate_content;
	}

	public void setOperate_content(String operate_content) {
		this.operate_content = operate_content;
	}

	public String getJyw() {
		return jyw;
	}

	public void setJyw(String jyw) {
		this.jyw = jyw;
	}

}
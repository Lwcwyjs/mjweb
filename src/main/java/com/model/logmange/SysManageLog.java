package com.model.logmange;

import javax.xml.bind.annotation.XmlElement;

public class SysManageLog {
	@XmlElement
	private Long num_id;//流水号
	@XmlElement
    private String user_id;//用户标识
	@XmlElement
    private String organization;//单位名称
	@XmlElement
    private String user_name;//用户名
	@XmlElement
	private String terminal_id;//终端标识
	@XmlElement
    private String operate_type;//操作类型0:登录日志 1:查询日志2:新增日志3修改日志4删除日志
	@XmlElement
    private String operate_time;//操作时间
	@XmlElement
    private String operate_condition;//操作内容
	@XmlElement
    private String operate_result;//操作结果1:成功 0:失败
	@XmlElement
    private String jyw;//校验位
	
	public Long getNum_id() {
		return num_id;
	}

	public void setNum_id(Long num_id) {
		this.num_id = num_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getOperate_type() {
		return operate_type;
	}

	public void setOperate_type(String operate_type) {
		this.operate_type = operate_type;
	}

	public String getOperate_time() {
		return operate_time;
	}

	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}

	public String getOperate_condition() {
		return operate_condition;
	}

	public void setOperate_condition(String operate_condition) {
		this.operate_condition = operate_condition;
	}

	public String getOperate_result() {
		return operate_result;
	}

	public void setOperate_result(String operate_result) {
		this.operate_result = operate_result;
	}

	public String getJyw() {
		return jyw;
	}

	public void setJyw(String jyw) {
		this.jyw = jyw;
	}

	
}
package com.model.logmange;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class SysInvokingLog implements Serializable{
	@XmlElement
    private String jkid;//接口id
	@XmlElement
    private String organization;//调用机构
	@XmlElement
    private String operate_time;//接口服务时间
	@XmlElement
    private String user_name;//请求方名称
	@XmlElement
    private String terminal_id;//终端标识
	@XmlElement
    private String operate_content;//接口服务条件
	@XmlElement
    private String operate_result;//接口服务结果
	@XmlElement
    private String jyw;//校验位
	
	@XmlElement
    private Long num_id;//流水号(长整型)
	public Long getNum_id() {
		return num_id;
	}
	public void setNum_id(Long num_id) {
		this.num_id = num_id;
	}
	public String getJkid() {
		return jkid;
	}
	public void setJkid(String jkid) {
		this.jkid = jkid;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getOperate_time() {
		return operate_time;
	}
	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
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
	public String getOperate_content() {
		return operate_content;
	}
	public void setOperate_content(String operate_content) {
		this.operate_content = operate_content;
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
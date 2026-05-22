package com.model.logmange;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class SysInterLog implements Serializable{
	@XmlElement
    private Long num_id;//流水号(长整型)
	@XmlElement
    private String interface_time;//接口服务时间
	@XmlElement
    private String requester;//请求方名称
	@XmlElement
    private String terminal_id;//终端标识
	@XmlElement
    private String interface_condition;//接口服务条件
	@XmlElement
    private String interface_result;//接口服务结果
	@XmlElement
    private String jyw;//校验位
	public Long getNum_id() {
		return num_id;
	}
	public void setNum_id(Long num_id) {
		this.num_id = num_id;
	}
	public String getInterface_time() {
		return interface_time;
	}
	public void setInterface_time(String interface_time) {
		this.interface_time = interface_time;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
	public String getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}
	public String getInterface_condition() {
		return interface_condition;
	}
	public void setInterface_condition(String interface_condition) {
		this.interface_condition = interface_condition;
	}
	public String getInterface_result() {
		return interface_result;
	}
	public void setInterface_result(String interface_result) {
		this.interface_result = interface_result;
	}
	public String getJyw() {
		return jyw;
	}
	public void setJyw(String jyw) {
		this.jyw = jyw;
	}

}
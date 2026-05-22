package com.model.configmange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="vehispara")
public class SysOptions {
	
	
	@XmlElement
	private String id;	     //varchar(32)

	@XmlElement
	private String option_kind;	     //varchar(100)
	
	@XmlElement
	private String option_value;	 //varchar(100)
	
	@XmlElement
	private String option_des;	     //varchar（100）
	
	@XmlElement
	private String option_organ;	 //varchar(8)
	
	@XmlElement
	private String option_params;	 //varchar(100)

	@XmlElement
	private String status;	         //char(1)

	@XmlTransient
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@XmlTransient
	public String getOption_kind() {
		return option_kind;
	}

	public void setOption_kind(String option_kind) {
		this.option_kind = option_kind;
	}

	@XmlTransient
	public String getOption_value() {
		return option_value;
	}

	public void setOption_value(String option_value) {
		this.option_value = option_value;
	}

	@XmlTransient
	public String getOption_des() {
		return option_des;
	}

	public void setOption_des(String option_des) {
		this.option_des = option_des;
	}

	@XmlTransient
	public String getOption_organ() {
		return option_organ;
	}

	public void setOption_organ(String option_organ) {
		this.option_organ = option_organ;
	}

	@XmlTransient
	public String getOption_params() {
		return option_params;
	}

	public void setOption_params(String option_params) {
		this.option_params = option_params;
	}

	@XmlTransient
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}







}

package com.model.configmange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="vehispara")
public class SysCode {
	
	@XmlElement
	private String id;// 	varchar(32)	not null
	
	@XmlElement
	private String oi_name;//	名
	
	@XmlElement
	private String oi_value;//	值
	
	@XmlElement
	private String oi_code;//	代码
	
	@XmlElement
	private Long seq;//	int	null
	
	@XmlTransient
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@XmlTransient
	public String getOi_name() {
		return oi_name;
	}
	public void setOi_name(String oi_name) {
		this.oi_name = oi_name;
	}
	
	@XmlTransient
	public String getOi_value() {
		return oi_value;
	}
	public void setOi_value(String oi_value) {
		this.oi_value = oi_value;
	}
	
	@XmlTransient
	public String getOi_code() {
		return oi_code;
	}
	public void setOi_code(String oi_code) {
		this.oi_code = oi_code;
	}
	
	@XmlTransient
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	
	
}

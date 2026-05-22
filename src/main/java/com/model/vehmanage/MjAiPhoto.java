package com.model.vehmanage;

import com.commons.annotation.DV;
import com.commons.utils.JaxbDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * @description：环保目录
 * @author：zengxj
 * @date：2016/4/08 16:51
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
public class MjAiPhoto implements Serializable {

	
    private String id;
    private String zpzl;
	
    private String zp;
	
    private String sbzt;
    
    private String sbjg;

	@DV(description="创建时间",nullable=true)
	@XmlElement
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	private Date cjsj;
	@DV(description="识别时间",nullable=true)
	@XmlElement
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	private Date sbsj;
    private String sbtype;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZpzl() {
		return zpzl;
	}

	public void setZpzl(String zpzl) {
		this.zpzl = zpzl;
	}

	public String getZp() {
		return zp;
	}

	public void setZp(String zp) {
		this.zp = zp;
	}

	public String getSbzt() {
		return sbzt;
	}

	public void setSbzt(String sbzt) {
		this.sbzt = sbzt;
	}

	public String getSbjg() {
		return sbjg;
	}

	public void setSbjg(String sbjg) {
		this.sbjg = sbjg;
	}

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	public Date getSbsj() {
		return sbsj;
	}

	public void setSbsj(Date sbsj) {
		this.sbsj = sbsj;
	}

	public String getSbtype() {
		return sbtype;
	}

	public void setSbtype(String sbtype) {
		this.sbtype = sbtype;
	}
}
package com.model.sysmanage;

import com.commons.annotation.DV;
import com.commons.utils.JaxbDateTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * @description：部门
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="vehcrpara")
public class MjOnlineQy implements Serializable {

	@DV(description = "企业编号", minLength = 6, maxLength = 30, nullable = false)
	@XmlElement
	private String organ;

	@DV(description = "企业名称", maxLength = 1, nullable = false)
	@XmlElement
	private String name;
	@DV(description = "简称", maxLength = 1, nullable = false)
	@XmlElement
	private String jc;

	@DV(description = "上级机构", maxLength = 3, nullable = false)
	@XmlElement
	private String porgan;

	@XmlElement
	private String  isonline;
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	private Date onlinetime;

	@XmlTransient
	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}
	@XmlTransient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@XmlTransient
	public String getJc() {
		return jc;
	}

	public void setJc(String jc) {
		this.jc = jc;
	}
	@XmlTransient
	public String getPorgan() {
		return porgan;
	}

	public void setPorgan(String porgan) {
		this.porgan = porgan;
	}
	@XmlTransient
	public String getIsonline() {
		return isonline;
	}

	public void setIsonline(String isonline) {
		this.isonline = isonline;
	}
	@XmlTransient
	public Date getOnlinetime() {
		return onlinetime;
	}

	public void setOnlinetime(Date onlinetime) {
		this.onlinetime = onlinetime;
	}
}
package com.model.count;

import com.commons.annotation.DV;
import com.commons.utils.JaxbDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;


/**
 * @description：用户
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="vehispara")
public class MjCdpf implements Serializable {

	@XmlElement
	private String id;
	

	@DV(description = "运输物", maxLength = 30, nullable = false)
	@XmlElement
	private String ysw;

	@DV(description="运输量",maxLength=10,nullable=true)
	@XmlElement
	private String ysl;

	@DV(description = "运输日期", nullable = false)
	@XmlElement
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	private Date yssj;

	@DV(description = "创建时间", nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	private Date cjsj;

	@DV(description = "所属机构",  maxLength = 30, nullable = false)
	@XmlElement
	private String organ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getYsw() {
		return ysw;
	}

	public void setYsw(String ysw) {
		this.ysw = ysw;
	}

	public String getYsl() {
		return ysl;
	}

	public void setYsl(String ysl) {
		this.ysl = ysl;
	}

	public Date getYssj() {
		return yssj;
	}

	public void setYssj(Date yssj) {
		this.yssj = yssj;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}
}
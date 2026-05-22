package com.model.local;

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
public class YuYue implements Serializable {

	@XmlElement
	private String dataid;
	

	@DV(description = "车牌号码", maxLength = 20, nullable = false)
	@XmlElement
	private String licenseplate;

	@DV(description = "车牌颜色", maxLength = 1, nullable = false)
	@XmlElement
	private String licenseplatecolor;

	@DV(description = "所属机构",  maxLength = 30, nullable = false)
	@XmlElement
	private String organ;

	@DV(description = "生效时间", nullable = false)
	@XmlElement
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date begintime;
	@DV(description = "终止时间", nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	private Date endtime;
	@DV(description = "创建时间", nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	private Date cjsj;
	@DV(description = "出入口编号", nullable = true)
	@XmlElement
	private String getecode;

	public String getDataid() {
		return dataid;
	}

	public void setDataid(String dataid) {
		this.dataid = dataid;
	}

	public String getLicenseplate() {
		return licenseplate;
	}

	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}

	public String getLicenseplatecolor() {
		return licenseplatecolor;
	}

	public void setLicenseplatecolor(String licenseplatecolor) {
		this.licenseplatecolor = licenseplatecolor;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	public String getGetecode() {
		return getecode;
	}

	public void setGetecode(String getecode) {
		this.getecode = getecode;
	}
}
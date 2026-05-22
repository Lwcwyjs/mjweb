package com.model.sysmanage;

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
 * @description：部门
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="vehcrpara")
public class MjDzxx implements Serializable {

	@DV(description = "id", maxLength = 32, nullable = false)
	@XmlElement
	private String id;
	@DV(description = "企业编号", minLength = 6, maxLength = 30, nullable = false)
	@XmlElement
	private String qybh;

	@DV(description = "出入口编号", maxLength = 1, nullable = false)
	@XmlElement
	private String crkbh;

	@DV(description = "道闸编号", maxLength = 3, nullable = false)
	@XmlElement
	private String dzbh;

	@DV(description = "道闸名称", maxLength = 50, nullable = false)
	@XmlElement
	private String dzmc;

	@DV(description = "道闸地址", maxLength = 200, nullable = false)
	@XmlElement
	private String dzdz;
	@DV(description = "机构经度", nullable = false, regexExpression = "(([1-9]{1}\\d*)|([0]{1}))\\.(\\d){6}$")
	@XmlElement
	private String lng;

	@DV(description = "机构纬度", nullable = false, regexExpression = "(([1-9]{1}\\d*)|([0]{1}))\\.(\\d){6}$")
	@XmlElement
	private String lat;

	@DV(description = "道闸负责人", maxLength = 10, nullable = false)
	@XmlElement
	private String dzfzr;

	@DV(description = "道闸负责人联系电话", maxLength = 20, nullable = false)
	@XmlElement
	private String dzfzrlxdh;
	@DV(description = "道闸运维单位名称", maxLength = 20, nullable = false)
	@XmlElement
	private String dzywdwmc;
	@DV(description = "道闸运维单位联系人", maxLength = 10, nullable = false)
	@XmlElement
	private String dzywdwlxr;
	@DV(description = "道闸运维单位联系电话", maxLength = 20, nullable = false)
	@XmlElement
	private String dzywdwlxdh;
	@DV(description = "上传状态", maxLength = 1, nullable = true)
	@XmlElement
	private String sczt;
	@DV(description = "上传结果", maxLength = 100, nullable = true)
	@XmlElement
	private String scjg;
	@DV(description = "创建时间", nullable = true)
	@XmlElement
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	private Date cjsj;
	@DV(description = "更新类型", maxLength = 1, nullable = true)
	@XmlElement
	private String gxlx;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQybh() {
		return qybh;
	}

	public void setQybh(String qybh) {
		this.qybh = qybh;
	}

	public String getCrkbh() {
		return crkbh;
	}

	public void setCrkbh(String crkbh) {
		this.crkbh = crkbh;
	}

	public String getDzbh() {
		return dzbh;
	}

	public void setDzbh(String dzbh) {
		this.dzbh = dzbh;
	}

	public String getDzmc() {
		return dzmc;
	}

	public void setDzmc(String dzmc) {
		this.dzmc = dzmc;
	}

	public String getDzdz() {
		return dzdz;
	}

	public void setDzdz(String dzdz) {
		this.dzdz = dzdz;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getDzfzr() {
		return dzfzr;
	}

	public void setDzfzr(String dzfzr) {
		this.dzfzr = dzfzr;
	}

	public String getDzfzrlxdh() {
		return dzfzrlxdh;
	}

	public void setDzfzrlxdh(String dzfzrlxdh) {
		this.dzfzrlxdh = dzfzrlxdh;
	}

	public String getDzywdwmc() {
		return dzywdwmc;
	}

	public void setDzywdwmc(String dzywdwmc) {
		this.dzywdwmc = dzywdwmc;
	}

	public String getDzywdwlxr() {
		return dzywdwlxr;
	}

	public void setDzywdwlxr(String dzywdwlxr) {
		this.dzywdwlxr = dzywdwlxr;
	}

	public String getDzywdwlxdh() {
		return dzywdwlxdh;
	}

	public void setDzywdwlxdh(String dzywdwlxdh) {
		this.dzywdwlxdh = dzywdwlxdh;
	}

	public String getSczt() {
		return sczt;
	}

	public void setSczt(String sczt) {
		this.sczt = sczt;
	}

	public String getScjg() {
		return scjg;
	}

	public void setScjg(String scjg) {
		this.scjg = scjg;
	}

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	public String getGxlx() {
		return gxlx;
	}

	public void setGxlx(String gxlx) {
		this.gxlx = gxlx;
	}
}
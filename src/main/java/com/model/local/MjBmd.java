package com.model.local;

import com.commons.annotation.DV;
import com.commons.utils.JaxbDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.util.ExcelColumn;

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
public class MjBmd implements Serializable {

	@XmlElement
	private String id;
	

	@DV(description = "车牌号码", maxLength = 20, nullable = false)
	@XmlElement
	@ExcelColumn(name = "车牌号码")
	private String cphm;

	@DV(description = "车牌颜色", maxLength = 1, nullable = false)
	@XmlElement
	@ExcelColumn(name = "号牌颜色")
	private String cpys;

	@DV(description = "车主名称", maxLength = 200, nullable = true)
	@XmlElement
	@ExcelColumn(name = "姓名")
	private String czmc;

	@DV(description = "联系电话", maxLength = 20, nullable = true)
	@XmlElement
	@ExcelColumn(name = "联系电话")
	private String lxdh;

	@DV(description = "备注", maxLength = 200,nullable = true)
	@XmlElement
	@ExcelColumn(name = "备注")
	private String bz;


	@DV(description = "所属机构",  maxLength = 30, nullable = false)
	@XmlElement
	private String organ;

	@DV(description = "生效时间", nullable = false)
	@XmlElement
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ExcelColumn(name = "开始时间")
	private Date sxsj;
	@DV(description = "终止时间", nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	@ExcelColumn(name = "结束时间")
	private Date zzsj;
	@DV(description = "创建时间", nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	private Date cjsj;
	@DV(description = "出入口编号", nullable = true)
	@XmlElement
	@ExcelColumn(name = "可通行门")
	private String crkbh;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCphm() {
		return cphm;
	}

	public void setCphm(String cphm) {
		this.cphm = cphm;
	}

	public String getCpys() {
		return cpys;
	}

	public void setCpys(String cpys) {
		this.cpys = cpys;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {
		this.czmc = czmc;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public Date getSxsj() {
		return sxsj;
	}

	public void setSxsj(Date sxsj) {
		this.sxsj = sxsj;
	}

	public Date getZzsj() {
		return zzsj;
	}

	public void setZzsj(Date zzsj) {
		this.zzsj = zzsj;
	}

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	public String getCrkbh() {
		return crkbh;
	}

	public void setCrkbh(String crkbh) {
		this.crkbh = crkbh;
	}
}
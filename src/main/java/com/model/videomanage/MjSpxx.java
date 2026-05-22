package com.model.videomanage;

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
public class MjSpxx implements Serializable {

	@DV(description="id",maxLength = 32,nullable=false)
	@XmlElement
	private String id;
	@DV(description="企业编号",minLength=6,maxLength=30,nullable=false)
	@XmlElement
    private String qybh;
    
	@DV(description="出入口编号",maxLength=1,nullable=false)
	@XmlElement
    private String crkbh;

	@DV(description="道闸编号",maxLength=3,nullable=false)
	@XmlElement
	private String dzbh;

	@DV(description="摄像头编号",maxLength=15,nullable=false)
	@XmlElement
    private String sxtbh;

	@DV(description="摄像头名称",maxLength=20,nullable=false)
	@XmlElement
    private String  sxtmc;

	@DV(description="ip地址",maxLength=15,nullable=false)
	@XmlElement
	private String  ip;
	@DV(description="用户名",maxLength=15,nullable=false)
	@XmlElement
	private String  username;

	@DV(description="密码",maxLength=20,nullable=false)
	@XmlElement
	private String pwd;
	@DV(description="实时播放路径",maxLength=200,nullable=false)
	@XmlElement
	private String realurl;
	@DV(description="回放播放路径",maxLength=200,nullable=false)
	@XmlElement
	private String replayurl;
	@DV(description="厂商名称",maxLength=20,nullable=true)
	@XmlElement
	private String csmc;
	@DV(description="型号",maxLength=20,nullable=true)
	@XmlElement
	private String xh;
	@DV(description="门口类型",maxLength=1,nullable=true)
	@XmlElement
	private String mklx;
	@DV(description="国标流编号",maxLength=50,nullable=true)
	@XmlElement
	private String gblbh;
	@DV(description="通道号",maxLength=50,nullable=true)
	@XmlElement
	private String tdh;
	@DV(description="设备序列号",maxLength=50,nullable=true)
	@XmlElement
	private String sbxlh;
	@DV(description="进出类型",maxLength=1,nullable=false)
	@XmlElement
	private String jclx;
	@DV(description="上传状态",maxLength=1,nullable=true)
	@XmlElement
	private String sczt;
	@DV(description="上传结果",maxLength=100,nullable=true)
	@XmlElement
	private String scjg;
	@DV(description="创建时间",nullable=true)
	@XmlElement
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	private Date cjsj;
	@DV(description="更新类型",maxLength=1,nullable=true)
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

	public String getSxtbh() {
		return sxtbh;
	}

	public void setSxtbh(String sxtbh) {
		this.sxtbh = sxtbh;
	}

	public String getSxtmc() {
		return sxtmc;
	}

	public void setSxtmc(String sxtmc) {
		this.sxtmc = sxtmc;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRealurl() {
		return realurl;
	}

	public void setRealurl(String realurl) {
		this.realurl = realurl;
	}

	public String getReplayurl() {
		return replayurl;
	}

	public void setReplayurl(String replayurl) {
		this.replayurl = replayurl;
	}

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getMklx() {
		return mklx;
	}

	public void setMklx(String mklx) {
		this.mklx = mklx;
	}

	public String getGblbh() {
		return gblbh;
	}

	public void setGblbh(String gblbh) {
		this.gblbh = gblbh;
	}

	public String getTdh() {
		return tdh;
	}

	public void setTdh(String tdh) {
		this.tdh = tdh;
	}

	public String getSbxlh() {
		return sbxlh;
	}

	public void setSbxlh(String sbxlh) {
		this.sbxlh = sbxlh;
	}

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
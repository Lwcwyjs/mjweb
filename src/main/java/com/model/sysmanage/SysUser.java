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
 * @description：用户
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="vehispara")
public class SysUser implements Serializable {

	@XmlElement
	private Long id;
	

	@DV(description = "登录名", maxLength = 64, nullable = false)
	@XmlElement
	private String loginname;

	@DV(description = "人员姓名", maxLength = 64, nullable = false)
	@XmlElement
	private String name;

	@DV(description = "密码", maxLength = 64, nullable = true)
	@XmlElement
	private String password;

	@XmlElement
	private Integer sex;

	@DV(description = "用户状态", nullable = false)
	@XmlElement
	private Integer status;


	@DV(description = "所属机构",  maxLength = 30, nullable = false)
	@XmlElement
	private String organ;

	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	private Date createdate;

	@DV(description = "电话", maxLength = 15, nullable = true)
	@XmlElement
	private String phone;

	// 查询条件
	private Date createdateend;

	// 关联内容--角色
	@DV(description = "角色", nullable = false)
	@XmlElement
	private Long role_id;
	
	@XmlElement
	private Integer errortimes;
	
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	private Date locktime;

	@DV(description = "账户有效期", nullable = false)
	@XmlElement
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date zhyxq;
	@DV(description = "最近一次登陆时间", nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	private Date zjdlsj;
	@DV(description = "最近一次登录IP", nullable = true)
	@XmlElement
	private String zjdlip;;
	@DV(description = "上次登录时间", nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	private Date scdlsj;
	@DV(description = "上次登录IP", nullable = true)
	@XmlElement
	private String scdlip;
	@DV(description = "校验位", nullable = true)
	@XmlElement
	private String jyw;

	private String online;

	@DV(description = "更新日期", nullable = true)
	@XmlElement
	private String gxrq;
	
	@DV(description = "账号到期天数", nullable = true)
	@XmlElement
	private String yhdqdays;
	
	@DV(description = "用户唯一标识", maxLength = 32, nullable = false)
	@XmlElement
	private String userid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreatedateend() {
		return createdateend;
	}

	public void setCreatedateend(Date createdateend) {
		this.createdateend = createdateend;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public Integer getErrortimes() {
		return errortimes;
	}

	public void setErrortimes(Integer errortimes) {
		this.errortimes = errortimes;
	}

	public Date getLocktime() {
		return locktime;
	}

	public void setLocktime(Date locktime) {
		this.locktime = locktime;
	}

	public Date getZhyxq() {
		return zhyxq;
	}

	public void setZhyxq(Date zhyxq) {
		this.zhyxq = zhyxq;
	}

	public Date getZjdlsj() {
		return zjdlsj;
	}

	public void setZjdlsj(Date zjdlsj) {
		this.zjdlsj = zjdlsj;
	}

	public String getZjdlip() {
		return zjdlip;
	}

	public void setZjdlip(String zjdlip) {
		this.zjdlip = zjdlip;
	}

	public Date getScdlsj() {
		return scdlsj;
	}

	public void setScdlsj(Date scdlsj) {
		this.scdlsj = scdlsj;
	}

	public String getScdlip() {
		return scdlip;
	}

	public void setScdlip(String scdlip) {
		this.scdlip = scdlip;
	}

	public String getJyw() {
		return jyw;
	}

	public void setJyw(String jyw) {
		this.jyw = jyw;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getGxrq() {
		return gxrq;
	}

	public void setGxrq(String gxrq) {
		this.gxrq = gxrq;
	}

	public String getYhdqdays() {
		return yhdqdays;
	}

	public void setYhdqdays(String yhdqdays) {
		this.yhdqdays = yhdqdays;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
package com.model.sysmanage;

import com.commons.annotation.DV;
import com.commons.utils.JaxbDateTimeAdapter;

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
public class SysOrganization implements Serializable {

	@DV(description="机构编码",minLength=6,maxLength=30,nullable=false)
	@XmlElement
    private String organ;

	@DV(description="新机构编码",minLength=6,maxLength=30,nullable=false)
	@XmlElement
	private String organnew;
    
	@DV(description="机构名称",maxLength=300,nullable=false)
	@XmlElement
    private String name;

	@DV(description="简称",maxLength=150,nullable=false)
	@XmlElement
	private String jc;

	@DV(description="机构类型",maxLength=1,nullable=false)
	@XmlElement
    private String organtype;    

	@DV(description="上级机构",maxLength=12,nullable=true)
	@XmlElement
    private String  porgan;

	@DV(description="上级机构名称",maxLength=12,nullable=true)
	@XmlElement
	private String  porganname;


    @XmlElement
    private Integer seq;


	@DV(description="状态",maxLength=1,nullable=false)
	@XmlElement
	private String status;

	@XmlElement
	private String  tyshxybm;
	@DV(description="机构经度",nullable=true,regexExpression="(([1-9]{1}\\d*)|([0]{1}))\\.(\\d){6}$")
	@XmlElement
	private String  lng;

	@DV(description="机构纬度",nullable=true,regexExpression="(([1-9]{1}\\d*)|([0]{1}))\\.(\\d){6}$")
	@XmlElement
	private String  lat;
	@DV(description="行政区划",nullable=true,regexExpression = "^\\d{6}$")
	@XmlElement
	private String  ssxq;

	@XmlElement
	private String  address;
	@XmlElement
	private String  frdb;
	@XmlElement
	private String  hylx;
	@XmlElement
	private String  hyfz;
	@XmlElement
	private String  jxfjgklx;
	@XmlElement
	private String  lxr;
	@XmlElement
	private String  lxrdh;
	@XmlElement
	private Integer  zhcrksl;
	@XmlElement
	private Integer  dzsl;
	@XmlElement
	private Integer  ysclsl;
	@XmlElement
	private Integer  cnysclsl;
	@XmlElement
	private Integer  fdlydjxsl;
	@XmlElement
	private String  isonline;
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	@XmlElement
	private Date onlinetime;

	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    @XmlElement
    private Date createdate;

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public String getOrgannew() {
		return organnew;
	}

	public void setOrgannew(String organnew) {
		this.organnew = organnew;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJc() {
		return jc;
	}

	public void setJc(String jc) {
		this.jc = jc;
	}

	public String getOrgantype() {
		return organtype;
	}

	public void setOrgantype(String organtype) {
		this.organtype = organtype;
	}

	public String getPorgan() {
		return porgan;
	}

	public void setPorgan(String porgan) {
		this.porgan = porgan;
	}

	public String getPorganname() {
		return porganname;
	}

	public void setPorganname(String porganname) {
		this.porganname = porganname;
	}


	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getTyshxybm() {
		return tyshxybm;
	}

	public void setTyshxybm(String tyshxybm) {
		this.tyshxybm = tyshxybm;
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

	public String getSsxq() {
		return ssxq;
	}

	public void setSsxq(String ssxq) {
		this.ssxq = ssxq;
	}

	public String getFrdb() {
		return frdb;
	}

	public void setFrdb(String frdb) {
		this.frdb = frdb;
	}

	public String getHylx() {
		return hylx;
	}

	public void setHylx(String hylx) {
		this.hylx = hylx;
	}

	public String getHyfz() {
		return hyfz;
	}

	public void setHyfz(String hyfz) {
		this.hyfz = hyfz;
	}

	public String getJxfjgklx() {
		return jxfjgklx;
	}

	public void setJxfjgklx(String jxfjgklx) {
		this.jxfjgklx = jxfjgklx;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	public String getLxrdh() {
		return lxrdh;
	}

	public void setLxrdh(String lxrdh) {
		this.lxrdh = lxrdh;
	}

	public Integer getZhcrksl() {
		return zhcrksl;
	}

	public void setZhcrksl(Integer zhcrksl) {
		this.zhcrksl = zhcrksl;
	}

	public Integer getDzsl() {
		return dzsl;
	}

	public void setDzsl(Integer dzsl) {
		this.dzsl = dzsl;
	}

	public Integer getYsclsl() {
		return ysclsl;
	}

	public void setYsclsl(Integer ysclsl) {
		this.ysclsl = ysclsl;
	}

	public Integer getCnysclsl() {
		return cnysclsl;
	}

	public void setCnysclsl(Integer cnysclsl) {
		this.cnysclsl = cnysclsl;
	}

	public Integer getFdlydjxsl() {
		return fdlydjxsl;
	}

	public void setFdlydjxsl(Integer fdlydjxsl) {
		this.fdlydjxsl = fdlydjxsl;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsonline() {
		return isonline;
	}

	public void setIsonline(String isonline) {
		this.isonline = isonline;
	}

	public Date getOnlinetime() {
		return onlinetime;
	}

	public void setOnlinetime(Date onlinetime) {
		this.onlinetime = onlinetime;
	}
}
package com.model.vehmanage;

import com.commons.annotation.DV;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @description：环保目录
 * @author：zengxj
 * @date：2016/4/08 16:51
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
public class TVehicles implements Serializable {

	@XmlElement	
    private String id;
	
	@DV(description="车辆型号",nullable=false)
	@XmlElement	
    private String clxh;
	@XmlElement	
    private String clmc;
	@XmlElement	
    private String fdjscc;
    @XmlElement	
    private String manuf;
    @XmlElement	
    private String manufid;
    @XmlElement	
    private String clsb;
    
	@DV(description="批准日期",nullable=false)
    @XmlElement	
	private String filename;
    @XmlElement	
   	private String filenameend;
	@DV(description="排放",nullable=false)
    @XmlElement	
    private String pf;
	@DV(description="状态",nullable=false)
    @XmlElement	
    private String status;
    @XmlElement	
    private String bz;
    @XmlElement	
    private String cllb;
    @XmlElement	
    private String vin;
    @XmlElement	
    private String sjly;
    @XmlElement	
	@DV(description="发动机型号",nullable=false)
    private String fdjxh;
	public String getFdjxh() {
		return fdjxh;
	}

	public void setFdjxh(String fdjxh) {
		this.fdjxh = fdjxh;
	}

	@XmlTransient
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@XmlTransient
	public String getClxh() {
		return clxh;
	}

	public void setClxh(String clxh) {
		this.clxh = clxh;
	}

	@XmlTransient
	public String getFdjscc() {
		return fdjscc;
	}

	public void setFdjscc(String fdjscc) {
		this.fdjscc = fdjscc;
	}
	@XmlTransient
	public String getManuf() {
		return manuf;
	}

	public void setManuf(String manuf) {
		this.manuf = manuf;
	}
	@XmlTransient
	public String getManufid() {
		return manufid;
	}

	public void setManufid(String manufid) {
		this.manufid = manufid;
	}
	@XmlTransient
	public String getClsb() {
		return clsb;
	}

	public void setClsb(String clsb) {
		this.clsb = clsb;
	}
	@XmlTransient
	public String getClmc() {
		return clmc;
	}

	public void setClmc(String clmc) {
		this.clmc = clmc;
	}

	@XmlTransient
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	@XmlTransient
	public String getFilenameend() {
		return filenameend;
	}

	public void setFilenameend(String filenameend) {
		this.filenameend = filenameend;
	}

	@XmlTransient
	public String getPf() {
		return pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}
	@XmlTransient
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@XmlTransient
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
	@XmlTransient
	public String getCllb() {
		return cllb;
	}

	public void setCllb(String cllb) {
		this.cllb = cllb;
	}
	@XmlTransient
	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
	@XmlTransient
	public String getSjly() {
		return sjly;
	}

	public void setSjly(String sjly) {
		this.sjly = sjly;
	}

	@Override
    public String toString() {
		return ToStringBuilder.reflectionToString(this);
    }
}
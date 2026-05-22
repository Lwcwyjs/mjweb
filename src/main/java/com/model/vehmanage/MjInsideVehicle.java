package com.model.vehmanage;

import com.commons.annotation.DV;
import com.commons.utils.JaxbDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name = "vehcrpara")
public class MjInsideVehicle implements Serializable {

    @DV(description="表id",maxLength=32,nullable=false)
    @XmlElement
    private String id;

    @DV(description="车牌号码",maxLength=32,nullable=true)
    @XmlElement
    private String cphm;

    @DV(description="环保登记号码",maxLength=32,nullable=true)
    @XmlElement
    private String hbdjhm;

    @DV(description="企业编号",maxLength=30,nullable=false)
    @XmlElement
    private String qybh;
    @DV(description="车辆识别代号",maxLength=20,nullable=false)
    @XmlElement
    private String clsbdh;
    @DV(description="车辆品牌型号",maxLength=100,nullable=false)
    @XmlElement
    private String clppxh;
    @DV(description="燃料种类",maxLength=2,nullable=true)
    @XmlElement
    private String rlzl;
    @DV(description="注册日期",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date ccdjrq;

    @DV(description="生产日期",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date scrq;
    @DV(description="排放标准",maxLength=2,nullable=false)
    @XmlElement
    private String pfbz;
    @DV(description="联网状态",maxLength=1,nullable=true)
    @XmlElement
    private String lwzt;
    @DV(description="所有人",maxLength=100,nullable=true)
    @XmlElement
    private String syr;
    @DV(description="发动机号",maxLength=20,nullable=true)
    @XmlElement
    private String fdjh;
    @DV(description="创建时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date cjsj;
    @DV(description="行驶证照片",maxLength=100,nullable=true)
    @XmlElement
    private String xszzp;
    @DV(description="随车照片照片",maxLength=100,nullable=true)
    @XmlElement
    private String scqdzp;
    @DV(description="上传状态",maxLength=1,nullable=true)
    @XmlElement
    private String sczt;
    @DV(description="上传结果",maxLength=1,nullable=true)
    @XmlElement
    private String scjg;
    @DV(description="上传时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date scsj;
    @XmlTransient
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @XmlTransient
    public String getCphm() {
        return cphm;
    }

    public void setCphm(String cphm) {
        this.cphm = cphm;
    }
    @XmlTransient
    public String getHbdjhm() {
        return hbdjhm;
    }

    public void setHbdjhm(String hbdjhm) {
        this.hbdjhm = hbdjhm;
    }
    @XmlTransient
    public String getQybh() {
        return qybh;
    }

    public void setQybh(String qybh) {
        this.qybh = qybh;
    }
    @XmlTransient
    public String getClsbdh() {
        return clsbdh;
    }

    public void setClsbdh(String clsbdh) {
        this.clsbdh = clsbdh;
    }
    @XmlTransient
    public String getClppxh() {
        return clppxh;
    }

    public void setClppxh(String clppxh) {
        this.clppxh = clppxh;
    }
    @XmlTransient
    public String getRlzl() {
        return rlzl;
    }

    public void setRlzl(String rlzl) {
        this.rlzl = rlzl;
    }
    @XmlTransient
    public Date getCcdjrq() {
        return ccdjrq;
    }

    public void setCcdjrq(Date ccdjrq) {
        this.ccdjrq = ccdjrq;
    }
    @XmlTransient
    public Date getScrq() {
        return scrq;
    }

    public void setScrq(Date scrq) {
        this.scrq = scrq;
    }
    @XmlTransient
    public String getPfbz() {
        return pfbz;
    }

    public void setPfbz(String pfbz) {
        this.pfbz = pfbz;
    }
    @XmlTransient
    public String getLwzt() {
        return lwzt;
    }

    public void setLwzt(String lwzt) {
        this.lwzt = lwzt;
    }
    @XmlTransient
    public String getSyr() {
        return syr;
    }

    public void setSyr(String syr) {
        this.syr = syr;
    }
    @XmlTransient
    public String getFdjh() {
        return fdjh;
    }

    public void setFdjh(String fdjh) {
        this.fdjh = fdjh;
    }
    @XmlTransient
    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }
    @XmlTransient
    public String getXszzp() {
        return xszzp;
    }

    public void setXszzp(String xszzp) {
        this.xszzp = xszzp;
    }
    @XmlTransient
    public String getScqdzp() {
        return scqdzp;
    }

    public void setScqdzp(String scqdzp) {
        this.scqdzp = scqdzp;
    }
    @XmlTransient
    public String getSczt() {
        return sczt;
    }

    public void setSczt(String sczt) {
        this.sczt = sczt;
    }
    @XmlTransient
    public String getScjg() {
        return scjg;
    }

    public void setScjg(String scjg) {
        this.scjg = scjg;
    }
    @XmlTransient
    public Date getScsj() {
        return scsj;
    }

    public void setScsj(Date scsj) {
        this.scsj = scsj;
    }
}

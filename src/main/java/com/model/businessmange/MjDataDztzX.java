package com.model.businessmange;

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
public class MjDataDztzX implements Serializable {

    @DV(description="表id",maxLength=32,nullable=false)
    @XmlElement
    private String id;

    @DV(description="车牌号码",maxLength=30,nullable=false)
    @XmlElement
    private String cphm;

    @DV(description="车牌颜色",maxLength=1,nullable=false)
    @XmlElement
    private String cpys;
    @DV(description="车辆识别代号",maxLength=1,nullable=false)
    @XmlElement
    private String clsbdh;
    @DV(description="初次登记日期",maxLength=1,nullable=false)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date ccdjrq;
    @DV(description="车辆品牌型号",maxLength=50,nullable=false)
    @XmlElement
    private String clppxh;
    @DV(description="燃料种类",maxLength=2,nullable=false)
    @XmlElement
    private String rlzl;
    @DV(description="车辆类型",maxLength=2,nullable=false)
    @XmlElement
    private String cllx;
    @DV(description="联网状态",maxLength=1,nullable=false)
    @XmlElement
    private String lwzt;
    @DV(description="排放标准",maxLength=1,nullable=false)
    @XmlElement
    private String pfbz;
    @DV(description="使用性质",maxLength=3,nullable=false)
    @XmlElement
    private String syxz;
    @DV(description="所有人",maxLength=3,nullable=false)
    @XmlElement
    private String syr;
    @DV(description="车队名称",maxLength=100,nullable=false)
    @XmlElement
    private String cdmc;
    @DV(description="fdjh",maxLength=50,nullable=false)
    @XmlElement
    private String fdjh;

    @DV(description="企业编号",maxLength=30,nullable=false)
    @XmlElement
    private String qybh;
    @DV(description="进场id",maxLength=1,nullable=true)
    @XmlElement
    private String jcid;
    @DV(description="进场出入口编号",maxLength=1,nullable=true)
    @XmlElement
    private String jccrkbh;
    @DV(description="进场道闸编号",maxLength=3,nullable=true)
    @XmlElement
    private String jcdzbh;
    @DV(description="进场运输货物名称",maxLength=20,nullable=true)
    @XmlElement
    private String jcyshwmc;
    @DV(description="进场运输量",maxLength=10,nullable=true)
    @XmlElement
    private String jcysl;
    @DV(description="进场运输单位",maxLength=10,nullable=true)
    @XmlElement
    private String jcysdw;
    @DV(description="出场id",maxLength=1,nullable=true)
    @XmlElement
    private String ccid;
    @DV(description="出场出入口编号",maxLength=1,nullable=true)
    @XmlElement
    private String cccrkbh;
    @DV(description="出场道闸编号",maxLength=3,nullable=true)
    @XmlElement
    private String ccdzbh;
    @DV(description="出场运输货物名称",maxLength=20,nullable=true)
    @XmlElement
    private String ccyshwmc;
    @DV(description="出场运输量",maxLength=10,nullable=true)
    @XmlElement
    private String ccysl;
    @DV(description="出场运输单位",maxLength=10,nullable=true)
    @XmlElement
    private String ccysdw;
    @DV(description="创建时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date cjsj;
    @DV(description="进场照片",maxLength=100,nullable=true)
    @XmlElement
    private String jczp;
    @DV(description="出厂照片",maxLength=100,nullable=true)
    @XmlElement
    private String cczp;
    @DV(description="随车清单照片",maxLength=100,nullable=true)
    @XmlElement
    private String scqdzp;
    @DV(description="行驶证照片",maxLength=100,nullable=true)
    @XmlElement
    private String xszzp;
    @DV(description="进场时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date jcsj;

    @DV(description="出厂时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date ccsj;
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
    public String getCpys() {
        return cpys;
    }

    public void setCpys(String cpys) {
        this.cpys = cpys;
    }
    @XmlTransient
    public String getCllx() {
        return cllx;
    }

    public void setCllx(String cllx) {
        this.cllx = cllx;
    }

    @XmlTransient
    public String getClsbdh() {
        return clsbdh;
    }

    public void setClsbdh(String clsbdh) {
        this.clsbdh = clsbdh;
    }
    @XmlTransient
    public Date getCcdjrq() {
        return ccdjrq;
    }

    public void setCcdjrq(Date ccdjrq) {
        this.ccdjrq = ccdjrq;
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
    public String getLwzt() {
        return lwzt;
    }

    public void setLwzt(String lwzt) {
        this.lwzt = lwzt;
    }
    @XmlTransient
    public String getPfbz() {
        return pfbz;
    }

    public void setPfbz(String pfbz) {
        this.pfbz = pfbz;
    }
    @XmlTransient
    public String getSyxz() {
        return syxz;
    }

    public void setSyxz(String syxz) {
        this.syxz = syxz;
    }
    @XmlTransient
    public String getSyr() {
        return syr;
    }

    public void setSyr(String syr) {
        this.syr = syr;
    }
    @XmlTransient
    public String getQybh() {
        return qybh;
    }

    public void setQybh(String qybh) {
        this.qybh = qybh;
    }
    @XmlTransient
    public String getJcid() {
        return jcid;
    }

    public void setJcid(String jcid) {
        this.jcid = jcid;
    }
    @XmlTransient
    public String getJccrkbh() {
        return jccrkbh;
    }

    public void setJccrkbh(String jccrkbh) {
        this.jccrkbh = jccrkbh;
    }
    @XmlTransient
    public String getJcdzbh() {
        return jcdzbh;
    }

    public void setJcdzbh(String jcdzbh) {
        this.jcdzbh = jcdzbh;
    }
    @XmlTransient
    public String getJcyshwmc() {
        return jcyshwmc;
    }

    public void setJcyshwmc(String jcyshwmc) {
        this.jcyshwmc = jcyshwmc;
    }
    @XmlTransient
    public String getJcysl() {
        return jcysl;
    }

    public void setJcysl(String jcysl) {
        this.jcysl = jcysl;
    }
    @XmlTransient
    public String getCcid() {
        return ccid;
    }

    public void setCcid(String ccid) {
        this.ccid = ccid;
    }
    @XmlTransient
    public String getCccrkbh() {
        return cccrkbh;
    }

    public void setCccrkbh(String cccrkbh) {
        this.cccrkbh = cccrkbh;
    }
    @XmlTransient
    public String getCcdzbh() {
        return ccdzbh;
    }

    public void setCcdzbh(String ccdzbh) {
        this.ccdzbh = ccdzbh;
    }
    @XmlTransient
    public String getCcyshwmc() {
        return ccyshwmc;
    }

    public void setCcyshwmc(String ccyshwmc) {
        this.ccyshwmc = ccyshwmc;
    }
    @XmlTransient
    public String getCcysl() {
        return ccysl;
    }

    public void setCcysl(String ccysl) {
        this.ccysl = ccysl;
    }
    @XmlTransient
    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }
    @XmlTransient
    public String getJczp() {
        return jczp;
    }

    public void setJczp(String jczp) {
        this.jczp = jczp;
    }
    @XmlTransient
    public String getCczp() {
        return cczp;
    }

    public void setCczp(String cczp) {
        this.cczp = cczp;
    }
    @XmlTransient
    public String getScqdzp() {
        return scqdzp;
    }

    public void setScqdzp(String scqdzp) {
        this.scqdzp = scqdzp;
    }
    @XmlTransient
    public String getXszzp() {
        return xszzp;
    }

    public void setXszzp(String xszzp) {
        this.xszzp = xszzp;
    }
    @XmlTransient
    public Date getJcsj() {
        return jcsj;
    }

    public void setJcsj(Date jcsj) {
        this.jcsj = jcsj;
    }
    @XmlTransient
    public Date getCcsj() {
        return ccsj;
    }

    public void setCcsj(Date ccsj) {
        this.ccsj = ccsj;
    }
    @XmlTransient
    public String getJcysdw() {
        return jcysdw;
    }

    public void setJcysdw(String jcysdw) {
        this.jcysdw = jcysdw;
    }
    @XmlTransient
    public String getCcysdw() {
        return ccysdw;
    }

    public void setCcysdw(String ccysdw) {
        this.ccysdw = ccysdw;
    }

    public String getCdmc() {
        return cdmc;
    }

    public void setCdmc(String cdmc) {
        this.cdmc = cdmc;
    }

    public String getFdjh() {
        return fdjh;
    }

    public void setFdjh(String fdjh) {
        this.fdjh = fdjh;
    }
}

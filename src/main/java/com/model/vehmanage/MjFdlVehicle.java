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
public class MjFdlVehicle implements Serializable {

    @DV(description="表id",maxLength=32,nullable=false)
    @XmlElement
    private String id;
    @DV(description="车牌号码",maxLength=32,nullable=true)
    @XmlElement
    private String cphm;
    @DV(description="环保登记号码",maxLength=32,nullable=true)
    @XmlElement
    private String hbdjhm;
    @DV(description="产品识别码",maxLength=64,nullable=true)
    @XmlElement
    private String cpsbm;
    @DV(description="机械环保代码",maxLength=64,nullable=true)
    @XmlElement
    private String jxhbdm;

    @DV(description="企业编号",maxLength=30,nullable=false)
    @XmlElement
    private String qybh;
    @DV(description="机械型号",maxLength=20,nullable=false)
    @XmlElement
    private String jxxh;
    @DV(description="燃料种类",maxLength=2,nullable=true)
    @XmlElement
    private String rlzl;

    @DV(description="生产日期",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date scrq;
    @DV(description="机械种类",maxLength=1,nullable=false)
    @XmlElement
    private String jxzl;
    @DV(description="排放标准",maxLength=2,nullable=false)
    @XmlElement
    private String pfbz;
    @DV(description="发动机生产厂",maxLength=100,nullable=true)
    @XmlElement
    private String fdjscc;
    @DV(description="所有人",maxLength=100,nullable=true)
    @XmlElement
    private String syr;
    @DV(description="发动机编号",maxLength=32,nullable=true)
    @XmlElement
    private String fdjbh;
    @DV(description="发动机型号",maxLength=32,nullable=true)
    @XmlElement
    private String fdjxh;
    @DV(description="创建时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date cjsj;
    @DV(description="发动机铭牌照片",maxLength=100,nullable=true)
    @XmlElement
    private String fdjmpzp;
    @DV(description="整车铭牌照片",maxLength=100,nullable=true)
    @XmlElement
    private String zcmpzp;
    @DV(description="环保标签照片",maxLength=100,nullable=true)
    @XmlElement
    private String hbbqzp;
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
    public String getHbdjhm() {
        return hbdjhm;
    }

    public void setHbdjhm(String hbdjhm) {
        this.hbdjhm = hbdjhm;
    }
    @XmlTransient
    public String getCpsbm() {
        return cpsbm;
    }

    public void setCpsbm(String cpsbm) {
        this.cpsbm = cpsbm;
    }
    @XmlTransient
    public String getJxhbdm() {
        return jxhbdm;
    }

    public void setJxhbdm(String jxhbdm) {
        this.jxhbdm = jxhbdm;
    }
    @XmlTransient
    public String getQybh() {
        return qybh;
    }

    public void setQybh(String qybh) {
        this.qybh = qybh;
    }
    @XmlTransient
    public String getJxxh() {
        return jxxh;
    }

    public void setJxxh(String jxxh) {
        this.jxxh = jxxh;
    }
    @XmlTransient
    public String getRlzl() {
        return rlzl;
    }

    public void setRlzl(String rlzl) {
        this.rlzl = rlzl;
    }
    @XmlTransient
    public Date getScrq() {
        return scrq;
    }

    public void setScrq(Date scrq) {
        this.scrq = scrq;
    }
    @XmlTransient
    public String getJxzl() {
        return jxzl;
    }

    public void setJxzl(String jxzl) {
        this.jxzl = jxzl;
    }
    @XmlTransient
    public String getPfbz() {
        return pfbz;
    }

    public void setPfbz(String pfbz) {
        this.pfbz = pfbz;
    }
    @XmlTransient
    public String getFdjscc() {
        return fdjscc;
    }

    public void setFdjscc(String fdjscc) {
        this.fdjscc = fdjscc;
    }
    @XmlTransient
    public String getSyr() {
        return syr;
    }

    public void setSyr(String syr) {
        this.syr = syr;
    }
    @XmlTransient
    public String getFdjbh() {
        return fdjbh;
    }

    public void setFdjbh(String fdjbh) {
        this.fdjbh = fdjbh;
    }
    @XmlTransient
    public String getFdjxh() {
        return fdjxh;
    }

    public void setFdjxh(String fdjxh) {
        this.fdjxh = fdjxh;
    }
    @XmlTransient
    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }
    @XmlTransient
    public String getFdjmpzp() {
        return fdjmpzp;
    }

    public void setFdjmpzp(String fdjmpzp) {
        this.fdjmpzp = fdjmpzp;
    }
    @XmlTransient
    public String getZcmpzp() {
        return zcmpzp;
    }

    public void setZcmpzp(String zcmpzp) {
        this.zcmpzp = zcmpzp;
    }
    @XmlTransient
    public String getHbbqzp() {
        return hbbqzp;
    }

    public void setHbbqzp(String hbbqzp) {
        this.hbbqzp = hbbqzp;
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
    @XmlTransient
    public String getCphm() {
        return cphm;
    }

    public void setCphm(String cphm) {
        this.cphm = cphm;
    }
}

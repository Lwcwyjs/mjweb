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
public class MjDataBase implements Serializable {

    @DV(description="表id",maxLength=32,nullable=false)
    @XmlElement
    private String id;

    @DV(description="流水号",maxLength=46,nullable=false)
    @XmlElement
    private String lsh;

    @DV(description="车牌号码",maxLength=30,nullable=false)
    @XmlElement
    private String cphm;

    @DV(description="车牌颜色",maxLength=1,nullable=false)
    @XmlElement
    private String cpys;

    @DV(description="企业编号",maxLength=30,nullable=false)
    @XmlElement
    private String qybh;

    @DV(description="出入口编号",maxLength=1,nullable=true)
    @XmlElement
    private String crkbh;
    @DV(description="摄像头编号",maxLength=20,nullable=true)
    @XmlElement
    private String sxtbh;
    @DV(description="道闸编号",maxLength=3,nullable=true)
    @XmlElement
    private String dzbh;
    @DV(description="进出类型",maxLength=1,nullable=true)
    @XmlElement
    private String jclx;
    @DV(description="摆杆状态",maxLength=1,nullable=true)
    @XmlElement
    private String bgzt;
    @DV(description="上传状态",maxLength=1,nullable=true)
    @XmlElement
    private String sczt;
    @DV(description="上传结果",maxLength=1,nullable=true)
    @XmlElement
    private String scjg;
    @DV(description="创建时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date cjsj;
    @DV(description="车头照片",maxLength=100,nullable=true)
    @XmlElement
    private String ctzp;
    @DV(description="车身照片",maxLength=100,nullable=true)
    @XmlElement
    private String cszp;
    @DV(description="车牌照片",maxLength=100,nullable=true)
    @XmlElement
    private String cpzp;
    @DV(description="上传时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date scsj;
    @DV(description="通过开始时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date tgkssj;

    @DV(description="通过结束时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date tgjssj;
    @DV(description="运输货物名称",maxLength=20,nullable=true)
    @XmlElement
    private String yshwmc;
    @DV(description="运输量",maxLength=10,nullable=true)
    @XmlElement
    private String ysl;
    @DV(description="运输单位",maxLength=10,nullable=true)
    @XmlElement
    private String ysdw;
    @DV(description="车道号",maxLength=2,nullable=true)
    @XmlElement
    private String cdh;
    @DV(description="排放标准",maxLength=2,nullable=true)
    @XmlElement
    private String pfbz;
    @DV(description="所有人",maxLength=2,nullable=true)
    @XmlElement
    private String syr;
    @DV(description="燃料种类",maxLength=2,nullable=true)
    @XmlElement
    private String rlzl;
    @DV(description="管控结果",maxLength=20,nullable=true)
    @XmlElement
    private String gkjg;
    @DV(description="通行id",maxLength=50,nullable=true)
    @XmlElement
    private String txid;

    @XmlTransient
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @XmlTransient
    public String getLsh() {
        return lsh;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
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
    public String getQybh() {
        return qybh;
    }

    public void setQybh(String qybh) {
        this.qybh = qybh;
    }
    @XmlTransient
    public String getCrkbh() {
        return crkbh;
    }

    public void setCrkbh(String crkbh) {
        this.crkbh = crkbh;
    }
    @XmlTransient
    public String getSxtbh() {
        return sxtbh;
    }

    public void setSxtbh(String sxtbh) {
        this.sxtbh = sxtbh;
    }
    @XmlTransient
    public String getDzbh() {
        return dzbh;
    }

    public void setDzbh(String dzbh) {
        this.dzbh = dzbh;
    }
    @XmlTransient
    public String getJclx() {
        return jclx;
    }

    public void setJclx(String jclx) {
        this.jclx = jclx;
    }
    @XmlTransient
    public String getBgzt() {
        return bgzt;
    }

    public void setBgzt(String bgzt) {
        this.bgzt = bgzt;
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
    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }
    @XmlTransient
    public String getCtzp() {
        return ctzp;
    }

    public void setCtzp(String ctzp) {
        this.ctzp = ctzp;
    }
    @XmlTransient
    public String getCszp() {
        return cszp;
    }

    public void setCszp(String cszp) {
        this.cszp = cszp;
    }
    @XmlTransient
    public String getCpzp() {
        return cpzp;
    }

    public void setCpzp(String cpzp) {
        this.cpzp = cpzp;
    }
    @XmlTransient
    public Date getScsj() {
        return scsj;
    }

    public void setScsj(Date scsj) {
        this.scsj = scsj;
    }
    @XmlTransient
    public Date getTgkssj() {
        return tgkssj;
    }

    public void setTgkssj(Date tgkssj) {
        this.tgkssj = tgkssj;
    }
    @XmlTransient
    public Date getTgjssj() {
        return tgjssj;
    }

    public void setTgjssj(Date tgjssj) {
        this.tgjssj = tgjssj;
    }
    @XmlTransient
    public String getYshwmc() {
        return yshwmc;
    }

    public void setYshwmc(String yshwmc) {
        this.yshwmc = yshwmc;
    }
    @XmlTransient
    public String getYsl() {
        return ysl;
    }

    public void setYsl(String ysl) {
        this.ysl = ysl;
    }
    @XmlTransient
    public String getCdh() {
        return cdh;
    }

    public void setCdh(String cdh) {
        this.cdh = cdh;
    }
    @XmlTransient
    public String getPfbz() {
        return pfbz;
    }

    public void setPfbz(String pfbz) {
        this.pfbz = pfbz;
    }
    @XmlTransient
    public String getRlzl() {
        return rlzl;
    }

    public void setRlzl(String rlzl) {
        this.rlzl = rlzl;
    }
    @XmlTransient
    public String getGkjg() {
        return gkjg;
    }

    public void setGkjg(String gkjg) {
        this.gkjg = gkjg;
    }
    @XmlTransient
    public String getYsdw() {
        return ysdw;
    }

    public void setYsdw(String ysdw) {
        this.ysdw = ysdw;
    }
    @XmlTransient
    public String getSyr() {
        return syr;
    }

    public void setSyr(String syr) {
        this.syr = syr;
    }
    @XmlTransient
    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }
}

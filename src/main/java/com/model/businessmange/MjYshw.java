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
public class MjYshw implements Serializable {

    @DV(description="表id",maxLength=32,nullable=false)
    @XmlElement
    private String id;

    @DV(description="车牌号码",maxLength=30,nullable=false)
    @XmlElement
    private String cphm;

    @DV(description="车牌颜色",maxLength=1,nullable=false)
    @XmlElement
    private String cpys;
    @DV(description="流水号",maxLength=30,nullable=false)
    @XmlElement
    private String lsh;
    @DV(description="通过时间",maxLength=1,nullable=false)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date tgkssj;
    @DV(description="运输货物名称",maxLength=50,nullable=false)
    @XmlElement
    private String yshwmc;
    @DV(description="进出类型",maxLength=2,nullable=false)
    @XmlElement
    private String jclx;
    @DV(description="运输量",maxLength=2,nullable=false)
    @XmlElement
    private String ysl;
    @DV(description="运输单位",maxLength=1,nullable=false)
    @XmlElement
    private String ysdw;
    @DV(description="所有人",maxLength=1,nullable=false)
    @XmlElement
    private String syr;
    @DV(description="道闸编号",maxLength=3,nullable=false)
    @XmlElement
    private String dzbh;
    @DV(description="上传状态",maxLength=3,nullable=false)
    @XmlElement
    private String sczt;
    @DV(description="上传结果",maxLength=3,nullable=false)
    @XmlElement
    private String scjg;
    @DV(description="企业编号",maxLength=3,nullable=false)
    @XmlElement
    private String qybh;
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
    public String getLsh() {
        return lsh;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
    }
    @XmlTransient
    public Date getTgkssj() {
        return tgkssj;
    }

    public void setTgkssj(Date tgkssj) {
        this.tgkssj = tgkssj;
    }
    @XmlTransient
    public String getYshwmc() {
        return yshwmc;
    }

    public void setYshwmc(String yshwmc) {
        this.yshwmc = yshwmc;
    }
    @XmlTransient
    public String getJclx() {
        return jclx;
    }

    public void setJclx(String jclx) {
        this.jclx = jclx;
    }
    @XmlTransient
    public String getYsl() {
        return ysl;
    }

    public void setYsl(String ysl) {
        this.ysl = ysl;
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
    public String getDzbh() {
        return dzbh;
    }

    public void setDzbh(String dzbh) {
        this.dzbh = dzbh;
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
    public String getQybh() {
        return qybh;
    }

    public void setQybh(String qybh) {
        this.qybh = qybh;
    }
}

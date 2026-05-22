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
public class MjVehicleO implements Serializable {

    @DV(description="表id",maxLength=32,nullable=false)
    @XmlElement
    private String id;

    @DV(description="车牌号码",maxLength=30,nullable=false)
    @XmlElement
    private String cphm;

    @DV(description="车牌颜色",maxLength=1,nullable=false)
    @XmlElement
    private String cpys;

    @DV(description="企业编号",maxLength=30,nullable=true)
    @XmlElement
    private String qybh;

    @DV(description="车辆类型",maxLength=3,nullable=false)
    @XmlElement
    private String cllx;
    @DV(description="车辆识别代号",minLength = 17,maxLength=17,nullable=false)
    @XmlElement
    private String clsbdh;
    @DV(description="车辆品牌型号",maxLength=100,nullable=false)
    @XmlElement
    private String clppxh;
    @DV(description="燃料种类",maxLength=2,nullable=false)
    @XmlElement
    private String rlzl;
    @DV(description="注册日期",nullable=false)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date ccdjrq;

    @DV(description="发证日期",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date fzrq;
    @DV(description="排放标准",maxLength=2,nullable=false)
    @XmlElement
    private String pfbz;
    @DV(description="联网状态",maxLength=1,nullable=false)
    @XmlElement
    private String lwzt;
    @DV(description="使用性质",maxLength=1,nullable=false)
    @XmlElement
    private String syxz;
    @DV(description="核定载质量",maxLength=10,nullable=true)
    @XmlElement
    private String hdzzl;
    @DV(description="使用性质id",maxLength=1,nullable=true)
    private String syxzid;
    @DV(description="车辆类型id",maxLength=3,nullable=true)
    private String cllxid;

    @DV(description="最大总质量",maxLength=10,nullable=true)
    @XmlElement
    private String zdzzl;
    @DV(description="核定载人数",maxLength=10,nullable=true)
    @XmlElement
    private String hdzrs;
    @DV(description="联系电话",maxLength=13,nullable=true)
    @XmlElement
    private String lxdh;
    @DV(description="地址",maxLength=100,nullable=true)
    @XmlElement
    private String address;
    @DV(description="所有人",maxLength=100,nullable=false)
    @XmlElement
    private String syr;
    @DV(description="发动机号",maxLength=100,nullable=false)
    @XmlElement
    private String fdjh;
    @DV(description="创建时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date cjsj;
    @DV(description="行驶证正面照片",maxLength=100,nullable=true)
    @XmlElement
    private String xszazp;
    @DV(description="行驶证副页照片",maxLength=100,nullable=true)
    @XmlElement
    private String xszbzp;
    @DV(description="随车清单照片",maxLength=100,nullable=true)
    @XmlElement
    private String scqdzp;
    @DV(description="车辆照片",maxLength=100,nullable=true)
    @XmlElement
    private String vehiclezp;
    @DV(description="车辆种类",maxLength=1,nullable=true)
    @XmlElement
    private String clzl;
    @DV(description="最新年检时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date zjnjsj;
    @DV(description="上传状态",maxLength=1,nullable=true)
    @XmlElement
    private String sczt;
    @DV(description="上传结果",maxLength=200,nullable=true)
    @XmlElement
    private String scjg;
    @DV(description="排放判定依据",maxLength=200,nullable=true)
    @XmlElement
    private String pfpdyj;
    @DV(description="排放判定依据种类",maxLength=2,nullable=true)
    @XmlElement
    private String pfpdyjzl;
    @DV(description="上传时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date scsj;

    @DV(description="处理标记",maxLength=2,nullable=true)
    @XmlElement
    private String clbj;

    @DV(description="处理结果",maxLength=100,nullable=true)
    @XmlElement
    private String cljg;
    @DV(description="道闸编号",maxLength=200,nullable=true)
    @XmlElement
    private String dzbhs;
    @DV(description="同步道闸编号",maxLength=200,nullable=true)
    @XmlElement
    private String tbdzbhs;
    @DV(description="更新状态",maxLength=2,nullable=true)
    @XmlElement
    private String gxzt;
    @DV(description="更新结果",maxLength=200,nullable=true)
    @XmlElement
    private String gxjg;
    @DV(description="新排放标准",maxLength=2,nullable=true)
    @XmlElement
    private String xpfbz;
    @DV(description="新排放判定依据",maxLength=200,nullable=true)
    @XmlElement
    private String xpfpdyj;
    @DV(description="新排放判定依据种类",maxLength=2,nullable=true)
    @XmlElement
    private String xpfpdyjzl;

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
    public String getQybh() {
        return qybh;
    }

    public void setQybh(String qybh) {
        this.qybh = qybh;
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
    public Date getScsj() {
        return scsj;
    }

    public void setScsj(Date scsj) {
        this.scsj = scsj;
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
    public String getClppxh() {
        return clppxh;
    }

    public void setClppxh(String clppxh) {
        this.clppxh = clppxh;
    }
    @XmlTransient
    public Date getCcdjrq() {
        return ccdjrq;
    }

    public void setCcdjrq(Date ccdjrq) {
        this.ccdjrq = ccdjrq;
    }
    @XmlTransient
    public String getLwzt() {
        return lwzt;
    }

    public void setLwzt(String lwzt) {
        this.lwzt = lwzt;
    }
    @XmlTransient
    public String getSyxz() {
        return syxz;
    }

    public void setSyxz(String syxz) {
        this.syxz = syxz;
    }
    @XmlTransient
    public String getHdzzl() {
        return hdzzl;
    }

    public void setHdzzl(String hdzzl) {
        this.hdzzl = hdzzl;
    }
    @XmlTransient
    public String getSyr() {
        return syr;
    }

    public void setSyr(String syr) {
        this.syr = syr;
    }
    @XmlTransient
    public String getXszazp() {
        return xszazp;
    }

    public void setXszazp(String xszazp) {
        this.xszazp = xszazp;
    }
    @XmlTransient
    public String getXszbzp() {
        return xszbzp;
    }

    public void setXszbzp(String xszbzp) {
        this.xszbzp = xszbzp;
    }
    @XmlTransient
    public String getScqdzp() {
        return scqdzp;
    }

    public void setScqdzp(String scqdzp) {
        this.scqdzp = scqdzp;
    }
    @XmlTransient
    public String getClzl() {
        return clzl;
    }

    public void setClzl(String clzl) {
        this.clzl = clzl;
    }
    @XmlTransient
    public Date getZjnjsj() {
        return zjnjsj;
    }

    public void setZjnjsj(Date zjnjsj) {
        this.zjnjsj = zjnjsj;
    }
    @XmlTransient
    public String getFdjh() {
        return fdjh;
    }

    public void setFdjh(String fdjh) {
        this.fdjh = fdjh;
    }
    @XmlTransient
    public String getZdzzl() {
        return zdzzl;
    }

    public void setZdzzl(String zdzzl) {
        this.zdzzl = zdzzl;
    }
    @XmlTransient
    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }
    @XmlTransient
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @XmlTransient
    public Date getFzrq() {
        return fzrq;
    }

    public void setFzrq(Date fzrq) {
        this.fzrq = fzrq;
    }
    @XmlTransient
    public String getPfpdyj() {
        return pfpdyj;
    }

    public void setPfpdyj(String pfpdyj) {
        this.pfpdyj = pfpdyj;
    }
    @XmlTransient
    public String getHdzrs() {
        return hdzrs;
    }

    public void setHdzrs(String hdzrs) {
        this.hdzrs = hdzrs;
    }
    @XmlTransient
    public String getPfpdyjzl() {
        return pfpdyjzl;
    }

    public void setPfpdyjzl(String pfpdyjzl) {
        this.pfpdyjzl = pfpdyjzl;
    }
    @XmlTransient
    public String getVehiclezp() {
        return vehiclezp;
    }

    public void setVehiclezp(String vehiclezp) {
        this.vehiclezp = vehiclezp;
    }
    @XmlTransient
    public String getClbj() {
        return clbj;
    }

    public void setClbj(String clbj) {
        this.clbj = clbj;
    }
    @XmlTransient
    public String getCljg() {
        return cljg;
    }

    public void setCljg(String cljg) {
        this.cljg = cljg;
    }
    @XmlTransient
    public String getSyxzid() {
        return syxzid;
    }

    public void setSyxzid(String syxzid) {
        this.syxzid = syxzid;
    }
    @XmlTransient
    public String getCllxid() {
        return cllxid;
    }

    public void setCllxid(String cllxid) {
        this.cllxid = cllxid;
    }
    @XmlTransient
    public String getDzbhs() {
        return dzbhs;
    }

    public void setDzbhs(String dzbhs) {
        this.dzbhs = dzbhs;
    }
    @XmlTransient
    public String getTbdzbhs() {
        return tbdzbhs;
    }

    public void setTbdzbhs(String tbdzbhs) {
        this.tbdzbhs = tbdzbhs;
    }
    @XmlTransient
    public String getGxzt() {
        return gxzt;
    }

    public void setGxzt(String gxzt) {
        this.gxzt = gxzt;
    }
    @XmlTransient
    public String getGxjg() {
        return gxjg;
    }

    public void setGxjg(String gxjg) {
        this.gxjg = gxjg;
    }

    @XmlTransient
    public String getXpfbz() {
        return xpfbz;
    }

    public void setXpfbz(String xpfbz) {
        this.xpfbz = xpfbz;
    }
    @XmlTransient
    public String getXpfpdyj() {
        return xpfpdyj;
    }

    public void setXpfpdyj(String xpfpdyj) {
        this.xpfpdyj = xpfpdyj;
    }
    @XmlTransient
    public String getXpfpdyjzl() {
        return xpfpdyjzl;
    }

    public void setXpfpdyjzl(String xpfpdyjzl) {
        this.xpfpdyjzl = xpfpdyjzl;
    }
}

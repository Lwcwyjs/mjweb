package com.model.vehmanage;

import com.commons.annotation.DV;
import com.commons.utils.JaxbDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

public class Vehicle implements Serializable {

    @DV(description="车辆编号",maxLength=30,nullable=false)
    @XmlElement
    private String vehiclebh;
    @DV(description="号牌号码",maxLength=30,nullable=false)
    @XmlElement
    private String hphm;

    @DV(description="号牌种类",maxLength=10,nullable=false)
    @XmlElement
    private String hpzlid;

    @DV(description="排放标准",maxLength=10,nullable=false)
    @XmlElement
    private String pfbz;
    @DV(description="车辆识别代号",maxLength=20,nullable=true)
    @XmlElement
    private String clsbdh;
    @DV(description="车辆类型",maxLength=100,nullable=true)
    @XmlElement
    private String cllx;
    @DV(description="车辆类型id",maxLength=3,nullable=true)
    @XmlElement
    private String cllxid;
    @DV(description="注册日期",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date ccdjrq;
    @DV(description="燃料种类",maxLength=2,nullable=true)
    @XmlElement
    private String rlzl;
    @DV(description="燃料种类id",maxLength=2,nullable=true)
    @XmlElement
    private String rlzlid;
    @DV(description="核定载质量",maxLength=2,nullable=true)
    @XmlElement
    private String hdzzl;
    @DV(description="排放标准id",maxLength=2,nullable=true)
    @XmlElement
    private String pfbzid;

    public String getVehiclebh() {
        return vehiclebh;
    }

    public void setVehiclebh(String vehiclebh) {
        this.vehiclebh = vehiclebh;
    }

    public String getHphm() {
        return hphm;
    }

    public void setHphm(String hphm) {
        this.hphm = hphm;
    }

    public String getHpzlid() {
        return hpzlid;
    }

    public void setHpzlid(String hpzlid) {
        this.hpzlid = hpzlid;
    }

    public String getPfbz() {
        return pfbz;
    }

    public void setPfbz(String pfbz) {
        this.pfbz = pfbz;
    }

    public String getClsbdh() {
        return clsbdh;
    }

    public void setClsbdh(String clsbdh) {
        this.clsbdh = clsbdh;
    }

    public String getCllx() {
        return cllx;
    }

    public void setCllx(String cllx) {
        this.cllx = cllx;
    }

    public String getCllxid() {
        return cllxid;
    }

    public void setCllxid(String cllxid) {
        this.cllxid = cllxid;
    }

    public Date getCcdjrq() {
        return ccdjrq;
    }

    public void setCcdjrq(Date ccdjrq) {
        this.ccdjrq = ccdjrq;
    }

    public String getRlzl() {
        return rlzl;
    }

    public void setRlzl(String rlzl) {
        this.rlzl = rlzl;
    }

    public String getRlzlid() {
        return rlzlid;
    }

    public void setRlzlid(String rlzlid) {
        this.rlzlid = rlzlid;
    }

    public String getHdzzl() {
        return hdzzl;
    }

    public void setHdzzl(String hdzzl) {
        this.hdzzl = hdzzl;
    }

    public String getPfbzid() {
        return pfbzid;
    }

    public void setPfbzid(String pfbzid) {
        this.pfbzid = pfbzid;
    }
}

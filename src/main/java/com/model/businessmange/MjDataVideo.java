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
public class MjDataVideo implements Serializable {

    @DV(description="表id",maxLength=32,nullable=false)
    @XmlElement
    private String id;

    @DV(description="baseid",maxLength=46,nullable=false)
    @XmlElement
    private String baseid;

    @DV(description="车牌号码",maxLength=30,nullable=false)
    @XmlElement
    private String cphm;

    @DV(description="车牌颜色",maxLength=1,nullable=false)
    @XmlElement
    private String cpys;

    @DV(description="企业编号",maxLength=30,nullable=false)
    @XmlElement
    private String qybh;

    @DV(description="进出类型",maxLength=1,nullable=true)
    @XmlElement
    private String jclx;
    @DV(description="录像编号",maxLength=1,nullable=true)
    @XmlElement
    private String lxbh;
    @DV(description="视频类型",maxLength=1,nullable=true)
    @XmlElement
    private String splx;
    @DV(description="视频Ip",maxLength=1,nullable=true)
    @XmlElement
    private String spip;
    @DV(description="视频通道",maxLength=1,nullable=true)
    @XmlElement
    private String sptd;
    @DV(description="平台路径",maxLength=1,nullable=true)
    @XmlElement
    private String ptlj;
    @DV(description="视频端口",maxLength=1,nullable=true)
    @XmlElement
    private String spport;
    @DV(description="视频用户名",maxLength=1,nullable=true)
    @XmlElement
    private String spuser;
    @DV(description="视频密码",maxLength=1,nullable=true)
    @XmlElement
    private String sppwd;
    @DV(description="视频路径",maxLength=1,nullable=true)
    @XmlElement
    private String splj;
    @DV(description="录像开始时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date lxkssj;
    @DV(description="录像结束时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date lxjssj;
    @DV(description="创建时间",nullable=true)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date cjsj;
    @XmlTransient
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @XmlTransient
    public String getBaseid() {
        return baseid;
    }

    public void setBaseid(String baseid) {
        this.baseid = baseid;
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
    public String getJclx() {
        return jclx;
    }

    public void setJclx(String jclx) {
        this.jclx = jclx;
    }
    @XmlTransient
    public String getLxbh() {
        return lxbh;
    }

    public void setLxbh(String lxbh) {
        this.lxbh = lxbh;
    }
    @XmlTransient
    public String getSplx() {
        return splx;
    }

    public void setSplx(String splx) {
        this.splx = splx;
    }
    @XmlTransient
    public String getSpip() {
        return spip;
    }

    public void setSpip(String spip) {
        this.spip = spip;
    }
    @XmlTransient
    public String getSptd() {
        return sptd;
    }

    public void setSptd(String sptd) {
        this.sptd = sptd;
    }
    @XmlTransient
    public String getSpport() {
        return spport;
    }

    public void setSpport(String spport) {
        this.spport = spport;
    }
    @XmlTransient
    public String getSpuser() {
        return spuser;
    }

    public void setSpuser(String spuser) {
        this.spuser = spuser;
    }
    @XmlTransient
    public String getSppwd() {
        return sppwd;
    }

    public void setSppwd(String sppwd) {
        this.sppwd = sppwd;
    }
    @XmlTransient
    public String getSplj() {
        return splj;
    }

    public void setSplj(String splj) {
        this.splj = splj;
    }
    @XmlTransient
    public Date getLxkssj() {
        return lxkssj;
    }

    public void setLxkssj(Date lxkssj) {
        this.lxkssj = lxkssj;
    }
    @XmlTransient
    public Date getLxjssj() {
        return lxjssj;
    }

    public void setLxjssj(Date lxjssj) {
        this.lxjssj = lxjssj;
    }
    @XmlTransient
    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }
    @XmlTransient
    public String getPtlj() {
        return ptlj;
    }

    public void setPtlj(String ptlj) {
        this.ptlj = ptlj;
    }
}

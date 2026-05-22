package com.model.count;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="head")
public class Cltxpf implements Serializable {

    private String pfbz;
    private String yshwmc;
    private String cpys;
    private String cls;
    private String ysl;

    private String jclc;
    private String yslhj;
    private String yslzb;

    public String getYshwmc() {
        return yshwmc;
    }

    public void setYshwmc(String yshwmc) {
        this.yshwmc = yshwmc;
    }

    public String getCpys() {
        return cpys;
    }

    public void setCpys(String cpys) {
        this.cpys = cpys;
    }

    public String getYsl() {
        return ysl;
    }

    public void setYsl(String ysl) {
        this.ysl = ysl;
    }

    public String getYslhj() {
        return yslhj;
    }

    public void setYslhj(String yslhj) {
        this.yslhj = yslhj;
    }

    public String getYslzb() {
        return yslzb;
    }

    public void setYslzb(String yslzb) {
        this.yslzb = yslzb;
    }

    public String getPfbz() {
        return pfbz;
    }

    public void setPfbz(String pfbz) {
        this.pfbz = pfbz;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getJclc() {
        return jclc;
    }

    public void setJclc(String jclc) {
        this.jclc = jclc;
    }
}

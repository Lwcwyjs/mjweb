package com.model.base;

import com.commons.annotation.DV;
import com.commons.utils.JaxbDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

public class DataTempVideo {
    private String id;
    private String lxurl;
    private String hcs_key;
    private String xzzt;
    private String xzjd;
    private String zpurl;
    @DV(description = "创建时间", nullable = false)
    @XmlElement
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    private Date cjsj;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLxurl() {
        return lxurl;
    }

    public void setLxurl(String lxurl) {
        this.lxurl = lxurl;
    }

    public String getHcs_key() {
        return hcs_key;
    }

    public void setHcs_key(String hcs_key) {
        this.hcs_key = hcs_key;
    }

    public String getXzzt() {
        return xzzt;
    }

    public void setXzzt(String xzzt) {
        this.xzzt = xzzt;
    }

    public String getXzjd() {
        return xzjd;
    }

    public void setXzjd(String xzjd) {
        this.xzjd = xzjd;
    }

    public String getZpurl() {
        return zpurl;
    }

    public void setZpurl(String zpurl) {
        this.zpurl = zpurl;
    }

    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }
}

package com.model.login;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="head")
public class PieVo implements Serializable {

    private String item;//统计类型：机构、车辆类型

    private Integer inum;//通过数

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getInum() {
        return inum;
    }

    public void setInum(Integer inum) {
        this.inum = inum;
    }
}

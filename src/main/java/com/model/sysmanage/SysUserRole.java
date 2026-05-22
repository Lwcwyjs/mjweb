package com.model.sysmanage;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @description：用户角色关联
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="vehispara")
public class SysUserRole implements Serializable {

    private Long id;  

    private Long user_id;

    private Long role_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_Id) {
        this.user_id = user_Id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_Id) {
        this.role_id = role_Id;
    }
    
    
    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", role_id=" + role_id +
                '}';
    }
}
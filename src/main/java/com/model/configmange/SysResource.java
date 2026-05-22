package com.model.configmange;

import com.commons.annotation.DV;
import com.commons.utils.JaxbDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @description：资源
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="vehispara")
public class SysResource implements Serializable {

    private Long id;

    @DV(description="资源名称",maxLength=30,nullable=true)
    @XmlElement
    private String name;

    @DV(description="访问地址",maxLength=100,nullable=true)
    @XmlElement
    private String url;

    @DV(description="资源的描述信息",maxLength=50,nullable=true)
    @XmlElement
    private String description;

    @DV(description="图标名称",maxLength=32,nullable=true)
    @XmlElement
    @JsonProperty("iconCls")
    private String icon;

    @DV(description="资源的描述信息",maxLength=8,nullable=true)
    @XmlElement
    private Long pid;

    @DV(description="排序号",maxLength=8,nullable=true)
    @XmlElement
    private Integer seq;

    @DV(description="状态",maxLength=8,nullable=true)
    @XmlElement
    private Integer status;

    @DV(description="资源类型",maxLength=8,nullable=true)
    @XmlElement
    private Integer resourcetype;

    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    @XmlElement
    private Date createdate;

    @DV(description="父节点名称",maxLength=8,nullable=true)
    @XmlElement
    private String pidname;

    @XmlTransient
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    @XmlTransient
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    @XmlTransient
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @XmlTransient
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    @XmlTransient
    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @XmlTransient
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @XmlTransient
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @XmlTransient
    public Integer getResourcetype() {
        return resourcetype;
    }

    public void setResourcetype(Integer resourcetype) {
        this.resourcetype = resourcetype;
    }

    @XmlTransient
    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    @XmlTransient
    public String getPidname() {
        return pidname;
    }

    public void setPidname(String pidname) {
        this.pidname = pidname;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
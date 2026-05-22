package com.commons.result;

import java.io.Serializable;
import java.util.List;

public class TreeVo implements Serializable {
	private static final long serialVersionUID = 980682543891282924L;
    private String id;
    private String text;
    private String state = "open";// open,closed
    private boolean checked = false;
    private Object attributes;
    private List<TreeVo> children;
    private String iconCls;
    private String pid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Object getAttributes() {
		return attributes;
	}
	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}
	public List<TreeVo> getChildren() {
		return children;
	}
	public void setChildren(List<TreeVo> children) {
		this.children = children;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getPcode() {
		return pid;
	}
	public void setPcode(String pid) {
		this.pid = pid;
	}

}

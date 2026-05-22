package com.commons.result;

import java.io.Serializable;
import java.util.List;

public class OrgTreeVo implements Serializable {
	private static final long serialVersionUID = 980682543891282924L;
    private String id;
    private String state = "open";// open,closed
    private boolean checked = false;
    private Object attributes;
    private List<OrgTreeVo> children;
    private String iconCls;
    private String pid;
	private String title;

	private boolean spread;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<OrgTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<OrgTreeVo> children) {
		this.children = children;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isSpread() {
		return spread;
	}

	public void setSpread(boolean spread) {
		this.spread = spread;
	}


}

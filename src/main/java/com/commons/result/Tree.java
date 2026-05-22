package com.commons.result;

import java.util.List;

/**
 * @description：TreeVO
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class Tree implements java.io.Serializable {

    private static final long serialVersionUID = 980682543891282923L;
    private Long id;
    private int tdh;
    private String code;
    private String text;

    private boolean spread;

    private String title;



    private String name;
    private String state = "open";// open,closed
    private boolean checked = false;
    private Object attributes;
    private List<Tree> children;
    private String iconCls;
    private String pcode;
    private String pid;
    private int ECode;
    private String Emsg;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getTdh() {
		return tdh;
	}

	public void setTdh(int tdh) {
		this.tdh = tdh;
	}

	public int getECode() {
		return ECode;
	}

	public void setECode(int eCode) {
		ECode = eCode;
	}

	public String getEmsg() {
		return Emsg;
	}

	public void setEmsg(String emsg) {
		Emsg = emsg;
	}
    public String getName() {
        return name;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }
	
	
}

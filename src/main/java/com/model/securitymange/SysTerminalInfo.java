package com.model.securitymange;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.commons.annotation.DV;
import com.commons.utils.JaxbDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @description：终端记录
 * @author：songdh 
 * @date：2018/6/13 17:13
 */
public class SysTerminalInfo {
	
	@XmlElement
	@DV(description="数据id",nullable=false)
	private String num_id;
	
	@XmlElement
	@DV(description="终端ID",nullable=false)
	private String terminal_id;
	
	@XmlElement
	@DV(description="失败次数",nullable=false)
	private Integer fail_times;
	
	@XmlElement
	@DV(description="更新时间",nullable=false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	private Date update_time;
	
	@XmlElement
	@DV(description="创建时间",nullable=false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	private Date create_time;
	
	@XmlElement
	@DV(description="锁定状态",nullable=false)
	private String islocked;
	
	@XmlElement
	@DV(description="校验位",nullable=false)
	private String jyw;
	
	@XmlElement
	@DV(description="锁定时间",nullable=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
	private Date lockedtime;

	public String getNum_id() {
		return num_id;
	}

	public void setNum_id(String num_id) {
		this.num_id = num_id;
	}

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	public Integer getFail_times() {
		return fail_times;
	}

	public void setFail_times(Integer fail_times) {
		this.fail_times = fail_times;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getIslocked() {
		return islocked;
	}

	public void setIslocked(String islocked) {
		this.islocked = islocked;
	}

	public String getJyw() {
		return jyw;
	}

	public void setJyw(String jyw) {
		this.jyw = jyw;
	}

	public Date getLockedtime() {
		return lockedtime;
	}

	public void setLockedtime(Date lockedtime) {
		this.lockedtime = lockedtime;
	}
	

	
	
}

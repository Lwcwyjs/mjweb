package com.task;

import com.service.base.PublicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;

@Component("ClearTask")
public class ClearTask {

	private final Logger logger = LoggerFactory.getLogger(ClearTask.class);

	static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	static final String DATE_FORMAT = "yyyy-MM-dd";

	private DateFormat format;

	private DateFormat formatDate;

	@Autowired
	private PublicService publicService;


	// 归档任务
	public void ClearTask() {
		try {
			String sql="insert into mj_online_qy select organ,name,porgan,jc,'0',getdate() from ai_sys_organization where organ not in(select organ from mj_online_qy where organtype='3') and organtype='3'";
			publicService.update(sql);
			sql="update mj_online_qy set isonline='0' where onlinetime<DATEADD(MINUTE, -10, GETDATE())";
			publicService.update(sql);
			sql="insert into mj_online_dz select a.organ+b.dzbh,a.organ,a.name,a.porgan,a.jc,b.dzmc,b.dzbh,'-1',getdate() from ai_sys_organization a,mj_dzxx b where a.organ=b.qybh"+
					" and a.organ+b.dzbh not in(select id from mj_online_dz)";
			publicService.update(sql);
			sql="update mj_online_dz set isonline='-1' where onlinetime<DATEADD(MINUTE, -10, GETDATE())";
			publicService.update(sql);

		} catch (Exception e) {
			logger.error("ClearTask执行异常:" + e.getMessage());
		}
	}
}

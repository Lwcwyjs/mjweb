package com.controller.securitymanage;

import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.model.securitymange.SysTerminalInfo;
import com.service.base.PublicService;
import com.service.logmanage.SysLogInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @description：操作日志查询
 * @author：JiaoSiYuan @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/admin/terminal")
public class SysTerminalinfoController extends BaseController {

	@Autowired
	private SysLogInfoService sysLogInfoService;

	@Autowired
	private PublicService publicService;

	/**
	 * 用户操作日志页面管理
	 *
	 * @return
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String manager(HttpServletRequest request) {
		return "securitymanage/sysTerminalInfo";
	}

	/**
	 * 用户操作日志列表页
	 *
	 * @param
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/dataGrid", method = RequestMethod.POST)
	@ResponseBody
	public Object dataGrid(SysTerminalInfo sysTerminalInfo, Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, "id", "asc");
		String strWhere = "";
		if (StringUtils.isNoneBlank(sysTerminalInfo.getTerminal_id())
				|| StringUtils.isNoneBlank(sysTerminalInfo.getIslocked())) {
			if ("null".equals(sysTerminalInfo.getTerminal_id()) || sysTerminalInfo.getTerminal_id() == null) {
				sysTerminalInfo.setTerminal_id("");
			}
			if ("null".equals(sysTerminalInfo.getIslocked()) || sysTerminalInfo.getIslocked() == null) {
				sysTerminalInfo.setIslocked("");
			}
			strWhere += String.format(" and terminal_id like '%%%s%%' and islocked like '%%%s%%'",
					sysTerminalInfo.getTerminal_id(), sysTerminalInfo.getIslocked());
		}
		String field = "num_id,terminal_id,islocked,update_time,create_time,fail_times";
		publicService.selectPageObjects(pageInfo, "AI_SYS_TERMINALINFO", field, strWhere, "num_id desc");
		return pageInfo;
	}

	/**
	 * 用户操作日志详情
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/unlock/{id}")
	@ResponseBody
	public Object unlock(@PathVariable String id) {
		SysTerminalInfo sysTerminalInfo = new SysTerminalInfo();
		sysTerminalInfo.setNum_id(id);
		sysTerminalInfo = (SysTerminalInfo) publicService.selectObject("AI_SYS_TERMINALINFO", "", "num_id", sysTerminalInfo);
		if (sysTerminalInfo != null) {
			sysTerminalInfo.setFail_times(0);
			sysTerminalInfo.setIslocked("0");
			sysTerminalInfo.setUpdate_time(new Date());
			publicService.update("AI_SYS_TERMINALINFO", "", "num_id", sysTerminalInfo);
			return renderSuccess("解锁成功！");
		} else {
			return renderError("解锁失败：无对应信息");
		}

	}
}

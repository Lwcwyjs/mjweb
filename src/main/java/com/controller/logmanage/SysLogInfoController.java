package com.controller.logmanage;

import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.model.logmange.LogCount;
import com.service.logmanage.SysLogInfoService;
import com.service.base.PublicService;
import com.model.logmange.SysLogInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @description：操作日志查询
 * @author：JiaoSiYuan @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/admin/loginfo")
public class SysLogInfoController extends BaseController {

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
		return "logmanage/sysLogInfo";
	}

	/**
	 * 用户操作日志列表页
	 *
	 * @param params
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/dataGrid", method = RequestMethod.POST)
	@ResponseBody
	public Object dataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, "id", "asc");
		String strWhere = "";
		String operate_condition="";
		if (params.containsKey("organization") && StringUtils.isNoneBlank(params.get("organization").toString())) {
			strWhere += String.format(" and organization like '%%%s%%'",params.get("organization").toString());
			operate_condition +="|"+params.get("organization").toString();
		}
		if (params.containsKey("user_name") && StringUtils.isNoneBlank(params.get("user_name").toString())) {
			strWhere += String.format(" and user_name like '%%%s%%'",params.get("user_name").toString());
			operate_condition +="|"+params.get("user_name").toString();
		}
		if (params.containsKey("terminal_id") && StringUtils.isNoneBlank(params.get("terminal_id").toString())) {
			strWhere += String.format(" and terminal_id like '%%%s%%'",params.get("terminal_id").toString());
			operate_condition +="|"+params.get("terminal_id").toString();
		}
		if (params.containsKey("operate_type") && StringUtils.isNoneBlank(params.get("operate_type").toString())) {
			strWhere += String.format(" and operate_type='%s'",params.get("operate_type").toString());
			operate_condition +="|"+params.get("operate_type").toString();
		}
		if (params.containsKey("operate_result") && StringUtils.isNoneBlank(params.get("operate_result").toString())) {
			strWhere += String.format(" and operate_result like '%%%s%%'",params.get("operate_result").toString());
			operate_condition +="|"+params.get("operate_result").toString();
		}
		if (params.containsKey("dateofstart") && StringUtils.isNoneBlank(params.get("dateofstart").toString())) {
			strWhere += String.format(" and operate_time  >  '%s000000'",
					params.get("dateofstart").toString().replace("-",""));
			operate_condition +="|"+params.get("dateofstart").toString();
		}
		if (params.containsKey("dateofend") && StringUtils.isNoneBlank(params.get("dateofend").toString())) {
			strWhere += String.format(" and operate_time  <= '%s235959' ",
					params.get("dateofend").toString().replace("-",""));
			operate_condition +="|"+params.get("dateofend").toString();
		}
		String field = "num_id,user_id,organization,user_name,terminal_id ,operate_type,operate_time,operate_condition,operate_result,terminal_type,jyw,operate_module";
		publicService.selectPageObjects(pageInfo, "AI_SYS_LOG_INFO", field, strWhere, "num_id desc");
		return pageInfo;
	}

	/**
	 * 查询用户操作日志详情
	 * 
	 * @param id
	 * @param
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(Long id, HttpServletRequest request) {
		List<String> bindResult = new ArrayList<String>();
		try {
			SysLogInfo loginfo = sysLogInfoService.findLogInfoById(id);
			loginfo.setOperate_condition(loginfo.getOperate_condition().replaceAll("\"", ""));
			loginfo.setOperate_condition(loginfo.getOperate_condition().replaceAll("\'", ""));
			loginfo.setOperate_condition(loginfo.getOperate_condition().replaceAll(" ", ""));
			loginfo.setOperate_condition(loginfo.getOperate_condition().replaceAll("<br>", ""));
			loginfo.setOperate_condition(loginfo.getOperate_condition().replaceAll("\n", ""));
			loginfo.setOperate_condition(loginfo.getOperate_condition().replaceAll("\r\n|\r|\n", ""));
			request.setAttribute("loginfo", loginfo);
			request.setAttribute("currentuser", this.getCurrentUser());
			request.setAttribute("oper", "edit");
		} catch (Exception ex) {
			bindResult.add("异常:" + ex.getMessage());
			ex.printStackTrace();
			request.setAttribute("ErrorMsg", bindResult);
		}
		return bindResult.size() == 0 ? "logmanage/sysLogInfoEdit" : "error/error";
	}

	/**
	 * 用户操作日志详情
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Object edit(SysLogInfo loginfo) {
		return renderSuccess("查询成功！");
	}

	@RequestMapping(value = "/basedata/{param}")
	@ResponseBody
	public Object getBaseData(@PathVariable String param) {
		return sysLogInfoService.findBaseData(param);
	}

	@RequestMapping(value = "/logBarCount", method = RequestMethod.POST)
	@ResponseBody
	public Object logBarCount(@RequestParam Map<String, Object> params) {
		List<Map> maps = new ArrayList<Map>();
		String strWhere = "";
		if (params.containsKey("organization") && StringUtils.isNoneBlank(params.get("organization").toString())) {
			strWhere += String.format(" and organization like '%%%s%%'",params.get("organization").toString());
		}
		if (params.containsKey("user_name") && StringUtils.isNoneBlank(params.get("user_name").toString())) {
			strWhere += String.format(" and user_name like '%%%s%%'",params.get("user_name").toString());
		}
		if (params.containsKey("terminal_id") && StringUtils.isNoneBlank(params.get("terminal_id").toString())) {
			strWhere += String.format(" and terminal_id like '%%%s%%'",params.get("terminal_id").toString());
		}
		if (params.containsKey("operate_type") && StringUtils.isNoneBlank(params.get("operate_type").toString())) {
			strWhere += String.format(" and operate_type='%s'",params.get("operate_type").toString());
		}
		if (params.containsKey("operate_result") && StringUtils.isNoneBlank(params.get("operate_result").toString())) {
			strWhere += String.format(" and operate_result like '%%%s%%'",params.get("operate_result").toString());
		}
		if (params.containsKey("dateofstart") && StringUtils.isNoneBlank(params.get("dateofstart").toString())) {
			strWhere += String.format(" and operate_time  >  '%s000000'",
					params.get("dateofstart").toString().replace("-",""));
		}
		if (params.containsKey("dateofend") && StringUtils.isNoneBlank(params.get("dateofend").toString())) {
			strWhere += String.format(" and operate_time  <= '%s235959' ",
					params.get("dateofend").toString().replace("-",""));
		}
//        List<Object> Objects = publicService.selectObjects("select operate_type type,count(*) num FROM AI_SYS_LOG_INFO t group by operate_type", LogCountEnty.class);
		List<LogCount> Objects = publicService.selectObjs("select operate_type type,count(*) num FROM AI_SYS_LOG_INFO t where 1=1 "+strWhere+" group by operate_type", LogCount.class);
		Map<String,Object> seriesBarMapData = new HashMap<String,Object>();
		List<Integer> numList = new ArrayList<Integer>();
		List<String> typeList = new ArrayList<String>();
		for (LogCount object : Objects) {
			numList.add(object.getNum());
			switch (object.getType().trim()){
				case "0" :
					typeList.add("登录操作");
					break;
				case "1" :
					typeList.add("查询操作");
					break;
				case "2" :
					typeList.add("新增操作");
					break;
				case "3" :
					typeList.add("修改操作");
					break;
				case "4" :
					typeList.add("删除操作");
					break;
				default:
					//typeList.add("未知类型-"+object.getType());
					break;

			}
		}
		seriesBarMapData.put("num", numList);
		seriesBarMapData.put("type", typeList);
		return seriesBarMapData;
	}
}

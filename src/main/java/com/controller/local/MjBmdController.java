package com.controller.local;

import com.alibaba.fastjson.JSONObject;
import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.local.MjBmd;
import com.model.sysmanage.SysOrganization;
import com.service.base.PublicService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
import com.service.sysmanage.SysRoleService;
import com.service.sysmanage.SysUserService;
import com.task.ExcelExportBmdTask;
import com.task.ExcelImportBmdTask;
import com.util.ExportProgressMonitor;
import com.util.ImportProgressMonitor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description：用户管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/local/bmd")
public class MjBmdController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysOrganizationService organService;

	@Autowired
	public SysOptionService sysOptionService;

	@Autowired
	private PublicService publicService;
	// Excel临时存储目录（需提前创建并配置）
	private static final String EXPORT_DIR = "D:/opt/temp-excel/";
	private final String importTempDir = "D:/opt/temp-import/"; // 导入临时目录
	// 直接创建线程池，不依赖 Spring 注入
	private final ThreadPoolTaskExecutor taskExecutor;

	// 构造函数中初始化线程池
	public MjBmdController() {
		taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setQueueCapacity(100);
		taskExecutor.setThreadNamePrefix("export-");
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		taskExecutor.initialize(); // 必须调用此方法启动线程池
	}


    @InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));

		/**
		 * 防止XSS攻击
		 */
		binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
	}

	/**
	 * 用户管理页
	 *
	 * @return
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String manager(HttpServletRequest request) {
		return "local/mjBmd";
	}


	/**
	 * 用户管理列表
	 *
	 * @param mjBmd
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/dataGrid", method = RequestMethod.POST)
	@ResponseBody
	public Object dataGrid(MjBmd mjBmd, Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		String strWhere = "";

		if (StringUtils.isNoneBlank(mjBmd.getOrgan())) {
			strWhere += String.format(" and organ='%s'",  mjBmd.getOrgan());
		} else {
			String strCurrentOrgan = getCurrentUserOrganCode();
			String strOrgans = organService.findChildrenCodes(strCurrentOrgan);
			if (StringUtils.isNoneBlank(strOrgans)) {
				strOrgans = strCurrentOrgan + "," + strOrgans;
				strOrgans = strOrgans.replace(",", "','");
				strWhere += String.format(" and organ in ('%s')",  strOrgans);
			} else {
				strWhere += String.format(" and organ='%s'",  strCurrentOrgan);
			}

		}
		if (StringUtils.isNoneBlank(mjBmd.getCphm())) {
			strWhere += String.format(" and cphm like '%%%s%%'",
					mjBmd.getCphm());
		}
		if (StringUtils.isNoneBlank(mjBmd.getCpys())) {
			strWhere += String.format(" and cpys like '%%%s%%'",
					mjBmd.getCpys());
		}
		String tblName = "(select id,d.name as organ,a.cphm,cpys,a.sxsj,a.zzsj,a.cjsj,lxdh,czmc,bz,crkbh " +
				" from (select * from mj_bmd  where 1=1 " + strWhere + ")a " +
				" left join (select * from ai_sys_organization) d on a.organ = d.organ)a";
		logger.info(tblName);
		publicService.selectPageObjects(pageInfo, tblName, "*", "", "cjsj desc");
		return pageInfo;
	}

	/**
	 * 添加用户页
	 *
	 * @return
	 */
	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public String addPage(HttpServletRequest request) {
		request.setAttribute("oper", "add");
		return "local/mjBmdEdit";
	}

	/**
	 * 添加用户
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(MjBmd mjBmd) {
		List<String> bindResult = new ArrayList<String>();

		try {
			mjBmd.setCjsj(new Date());
			UUID uuid = UUID.randomUUID();
			String uuids = uuid.toString().replaceAll("-", "");
			mjBmd.setId(uuids);

			bindResult = ValidateService.valid(mjBmd);

			if (bindResult.size() == 0) {
				// 验证用户是否已存在
				MjBmd mj = publicService.selectObj("mj_bmd","","cphm,cpys",mjBmd);
				if (mj != null) {
					bindResult.add("该车已存在，不能重复创建");
				}
				if (bindResult.size() == 0) {
					publicService.insert("mj_bmd","","",mjBmd);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			bindResult.add("异常：" + e.getMessage());
		}

		return bindResult.size() == 0 ? renderSuccess("新建成功！") : renderError(bindResult.toString());
	}

	/**
	 * 编辑用户页
	 * 
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(String id, HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		MjBmd mjBmd = publicService.selectObj("select * from mj_bmd where id='"+id+"'",MjBmd.class);
		request.setAttribute("oper", "edit");
		request.setAttribute("mjbmd", mjBmd);
		SysOrganization sysOrganization=organService.findOrganizationByCode(mjBmd.getOrgan());
		request.setAttribute("organname",sysOrganization.getJc());
		request.setAttribute("sxsj", sdf.format(null == mjBmd.getSxsj() ? new Date() : mjBmd.getSxsj()));
		request.setAttribute("zzsj", sdf.format(null == mjBmd.getZzsj() ? new Date() : mjBmd.getZzsj()));
		return "local/mjBmdEdit";
	}

	/**
	 * 编辑用户
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Object edit(MjBmd mjBmd) {
		List<String> bindResult = new ArrayList<String>();

		try {
			bindResult = ValidateService.valid(mjBmd);
			mjBmd.setCjsj(new Date());
			if (bindResult.size() == 0) {
				publicService.update("mj_bmd","","id",mjBmd);
			}

		} catch (Exception e) {
			e.printStackTrace();
			bindResult.add("异常：" + e.getMessage());
		}

		return bindResult.size() == 0 ? renderSuccess("修改成功！") : renderError(bindResult.toString());
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(String id) {
		try {
			publicService.delete("delete from mj_bmd where id='"+id+"'");
			return renderSuccess("删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("删除异常：" + e.getMessage());
		}
	}
	/**
	 * 接口1：发起导出（生成任务ID，启动异步任务）
	 */
	@RequestMapping(value = "/startExport", method = RequestMethod.POST)
	@ResponseBody
	public Object startExport(@RequestParam Map<String, Object> params) {
		if (params.containsKey("organ") && params.get("organ").toString().equals("")) {
			String organ = getCurrentUserOrganCode();
			params.put("organ", organ);
		}
		JSONObject dataJson = new JSONObject();
		dataJson.put("code", "0");
		dataJson.put("msg", "失败");
		dataJson.put("taskId", "");
		try {
			// 生成任务ID
			String taskId = ExportProgressMonitor.getInstance().generateTaskId();
			// 存储导出参数
			ExportProgressMonitor.getInstance().setParams(taskId, params);
			// 启动异步导出任务
			taskExecutor.submit(new ExcelExportBmdTask(
					taskId, EXPORT_DIR,
					sysOptionService, publicService, organService, logger
			));
			dataJson.put("code", "1");
			dataJson.put("msg", "启动成功");
			dataJson.put("taskId", taskId); // 返回任务ID给前端
		} catch (Exception e) {
			dataJson.put("code", "0");
			dataJson.put("msg", "导出任务启动失败");
		}
		return dataJson.toJSONString();
	}

	/**
	 * 接口2：查询导出进度
	 */
	@RequestMapping(value = "/exportProgress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> exportProgress(@RequestParam Map<String, Object> params) {
		String taskId=params.get("taskId").toString();
		Map<String, Object> result = new HashMap<>();
		int progress = ExportProgressMonitor.getInstance().getProgress(taskId);
		result.put("progress", progress);
		return result;
	}

	/**
	 * 接口3：下载导出文件
	 */
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
	public void downloadExcel(@RequestParam String taskId, HttpServletResponse response) {
		try {
			String filePath = ExportProgressMonitor.getInstance().getFilepath(taskId);
			if (filePath == null || !new File(filePath).exists()) {
				response.sendError(404, "文件不存在");
				return;
			}
			// 设置响应头，触发下载
			String fileName = new File(filePath).getName();
			response.setHeader("Content-Disposition", "attachment;filename=" +
					new String(fileName.getBytes("gb2312"), "ISO8859-1"));
			response.setContentType("application/vnd.ms-excel");
			// 写入文件流
			try (FileInputStream fis = new FileInputStream(filePath);
				 OutputStream os = response.getOutputStream()) {
				byte[] buffer = new byte[1024];
				int len;
				while ((len = fis.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}
			}
			// 下载完成后清理任务（可选）
			ExportProgressMonitor.getInstance().clearTask(taskId);
		} catch (Exception e) {
			logger.error("文件下载失败：" + e.getMessage(), e);
		}
	}


	// 1. 上传Excel文件，启动导入任务
	@RequestMapping("/startImport")
	@ResponseBody
	public Object startImport(@RequestParam("file") MultipartFile file) throws IOException {
		JSONObject dataJson = new JSONObject();
		dataJson.put("code", "1");
		dataJson.put("msg", "成功");
		dataJson.put("taskId", "");
		// 校验文件格式
		String fileName = file.getOriginalFilename();
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			dataJson.put("code", "0");
			dataJson.put("msg", "请上传Excel文件（.xls/.xlsx）");
			return dataJson.toJSONString();
		}

		// 生成任务ID（与导出一致的生成规则）
		String taskId = UUID.randomUUID().toString().replace("-", "");
		// 保存文件到临时目录
		File tempDir = new File(importTempDir);
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
		String importFilePath = importTempDir + taskId + "_" + fileName;
		file.transferTo(new File(importFilePath));

		// 提交异步任务
		ExcelImportBmdTask importTask = new ExcelImportBmdTask(
				taskId, importFilePath, publicService, organService, logger
		);
		taskExecutor.submit(importTask);

		// 记录任务参数（复用导出的进度监控）
		Map<String, Object> params = new HashMap<>();
		params.put("fileName", fileName);
		ImportProgressMonitor.getInstance().setParams(taskId, params);
		dataJson.put("code", "1");
		dataJson.put("msg", "导入任务已启动");
		dataJson.put("taskId",taskId);
		return dataJson.toJSONString();
	}

	// 2. 轮询导入进度（复用导出的进度查询接口，无需额外开发）
	@RequestMapping("/importProgress")
	@ResponseBody
	public Object importProgress(@RequestParam("taskId") String taskId) {
		JSONObject dataJson = new JSONObject();
		dataJson.put("code", "1");
		dataJson.put("msg", "成功");
		int progress = ImportProgressMonitor.getInstance().getProgress(taskId);
		String msg = ImportProgressMonitor.getInstance().getMsg(taskId);
		String errorMsg = ImportProgressMonitor.getInstance().getErrorMsg(taskId);
		dataJson.put("code", "0");
		dataJson.put("msg", errorMsg);
		dataJson.put("progress", progress);
		return dataJson.toJSONString();
	}

	// 3. 下载错误报表（可选，用于下载无效数据）
	@RequestMapping("/downloadErrorExcel")
	public void downloadErrorExcel(@RequestParam("taskId") String taskId, HttpServletResponse response) throws IOException {
		String errorFilePath = importTempDir + taskId + "_error.xls";
		File errorFile = new File(errorFilePath);
		// 下载逻辑（与导出文件下载一致）
		if (errorFile.exists()) {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=error_data.xls");
			FileUtils.copyFile(errorFile, response.getOutputStream());
			FileUtils.deleteQuietly(errorFile);
		}
	}
}

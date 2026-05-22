package com.controller.vehmanage;

import com.alibaba.fastjson.JSONObject;
import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.sysmanage.MjDzxx;
import com.model.sysmanage.SysOrganization;
import com.model.vehmanage.MjVehicle;
import com.service.base.PublicService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
import com.service.sysmanage.SysRoleService;
import com.service.sysmanage.SysUserService;
import com.task.ExcelExportVehicleTask;
import com.util.ExportProgressMonitor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description：用户管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/business/mjvehicle")
public class MjVehicleController extends BaseController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    public SysOrganizationService sysOrganizationService;

    @Autowired
    private PublicService publicService;
    @Autowired
    public SysOptionService sysOptionService;


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
    // Excel临时存储目录（需提前创建并配置）
    private static final String EXPORT_DIR = "D:/opt/temp-excel/";
    // 直接创建线程池，不依赖 Spring 注入
    private final ThreadPoolTaskExecutor taskExecutor;

    // 构造函数中初始化线程池
    public MjVehicleController() {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("export-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize(); // 必须调用此方法启动线程池
    }
    /**
     * 接口1：发起导出（生成任务ID，启动异步任务）
     */
    @RequestMapping(value = "/startExport", method = RequestMethod.POST)
    @ResponseBody
    public Object startExport(@RequestParam Map<String, Object> params) {
        if (params.containsKey("qybh") && params.get("qybh").toString().equals("")) {
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
            taskExecutor.submit(new ExcelExportVehicleTask(
                    taskId, EXPORT_DIR,
                    sysOptionService, publicService, sysOrganizationService, logger
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
    /**
     * 用户管理页
     *
     * @return
     */
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String manager(HttpServletRequest request) {
        return "vehmanage/MjVehicle";
    }
    @RequestMapping(value = "/managerGL", method = RequestMethod.GET)
    public String managerGL(HttpServletRequest request) {
        return "vehmanage/MjVehicleGL";
    }

    @RequestMapping(value = "/dataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object dataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        String strWhere = "";
        if (params.containsKey("qybh") && params.get("qybh").toString().equals("")) {
            String organ = getCurrentUserOrganCode();
            SysOrganization sysOrganization= sysOrganizationService.findOrganizationByCode(organ);
            if(sysOrganization!=null){
                if(!sysOrganization.getPorgan().equals("-1")){
                    String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
                    if (StringUtils.isNoneBlank(strOrgans)) {
                        strOrgans = params.get("qybh").toString() + "," + strOrgans;
                        strOrgans = strOrgans.replace(",", "','");
                        strWhere += String.format(" and qybh in ('%s')", strOrgans);
                    } else {
                        strWhere += String.format(" and qybh='%s'", organ);
                    }
                }
            }

        } else if (params.containsKey("qybh") && (StringUtils.isNoneBlank(params.get("qybh").toString()))) {
            String organ = params.get("qybh").toString();
            String strOrgans = sysOrganizationService.findChildrenCodes(organ);
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = organ + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", organ);
            }
        }
        if (params.containsKey("pfbz") && StringUtils.isNoneBlank(params.get("pfbz").toString())) {
            strWhere += String.format(" and pfbz='%s'", params.get("pfbz").toString());
        }
        if (params.containsKey("sczt") && StringUtils.isNoneBlank(params.get("sczt").toString())) {
            strWhere += String.format(" and sczt='%s'", params.get("sczt").toString());
        }
        if (params.containsKey("syxz") && StringUtils.isNoneBlank(params.get("syxz").toString())) {
            strWhere += String.format(" and syxz='%s'", params.get("syxz").toString());
        }
        if (params.containsKey("cphm") && StringUtils.isNoneBlank(params.get("cphm").toString())) {
            strWhere += " and cphm like '%" + params.get("cphm").toString() + "%'";
        }
        if (params.containsKey("cpys") && StringUtils.isNoneBlank(params.get("cpys").toString())) {
            String spzl = params.get("cpys").toString();
            strWhere += String.format(" and cpys='%s'", spzl);
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and cjsj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and cjsj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        String field = "*";
        publicService.selectPageObjects(pageInfo, "mj_vehicle", field, strWhere, "cjsj desc");
        return pageInfo;
    }

    /**
     * 添加用户页
     *
     * @return
     */
    @RequestMapping(value = "/addPageGL", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        request.setAttribute("mjvehicle",new MjVehicle());
        String organ = getCurrentUserOrganCode();
        String organname = sysOrganizationService.getQyjcByQybh(organ);
        request.setAttribute("organname",organname);
        request.setAttribute("oper", "add");
        return "vehmanage/MjVehicleEditGL";
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
        MjVehicle mjVehicle = new MjVehicle();
        mjVehicle.setId(id);
        mjVehicle = publicService.selectObj("mj_vehicle", "", "id", mjVehicle);
        String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
        if(mjVehicle.getXszazp()!=null&&!mjVehicle.getXszazp().equals("")){
            mjVehicle.setXszazp(photourl+mjVehicle.getXszazp());
        }
        if(mjVehicle.getXszbzp()!=null&&!mjVehicle.getXszbzp().equals("")){
            mjVehicle.setXszbzp(photourl+mjVehicle.getXszbzp());
        }
        if(mjVehicle.getScqdzp()!=null&&!mjVehicle.getScqdzp().equals("")){
            mjVehicle.setScqdzp(photourl+mjVehicle.getScqdzp());
        }
        if(mjVehicle.getVehiclezp()!=null&&!mjVehicle.getVehiclezp().equals("")){
            mjVehicle.setVehiclezp(photourl+mjVehicle.getVehiclezp());
        }
        request.setAttribute("mjvehicle", mjVehicle);
        String organname = sysOrganizationService.getQyjcByQybh(mjVehicle.getQybh());
        request.setAttribute("organname", organname);
        request.setAttribute("oper", "edit");

        return "vehmanage/MjVehicleEdit";
    }
    /**
     * 编辑用户页
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping("/editPageGL")
    public String editPageGL(String id, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        MjVehicle mjVehicle = new MjVehicle();
        mjVehicle.setId(id);
        mjVehicle = publicService.selectObj("mj_vehicle", "", "id", mjVehicle);
        String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
        if(mjVehicle.getXszazp()!=null&&!mjVehicle.getXszazp().equals("")){
            mjVehicle.setXszazp(photourl+mjVehicle.getXszazp());
        }
        if(mjVehicle.getXszbzp()!=null&&!mjVehicle.getXszbzp().equals("")){
            mjVehicle.setXszbzp(photourl+mjVehicle.getXszbzp());
        }
        if(mjVehicle.getScqdzp()!=null&&!mjVehicle.getScqdzp().equals("")){
            mjVehicle.setScqdzp(photourl+mjVehicle.getScqdzp());
        }
        if(mjVehicle.getVehiclezp()!=null&&!mjVehicle.getVehiclezp().equals("")){
            mjVehicle.setVehiclezp(photourl+mjVehicle.getVehiclezp());
        }
        request.setAttribute("mjvehicle", mjVehicle);
        String organname = sysOrganizationService.getQyjcByQybh(mjVehicle.getQybh());
        request.setAttribute("organname", organname);
        request.setAttribute("oper", "editGL");

        return "vehmanage/MjVehicleEditGL";
    }

    /**
     * 编辑用户
     *
     * @param
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(MjVehicle mjVehicle) {
        List<String> bindResult = new ArrayList<String>();

        try {
            bindResult = ValidateService.valid(mjVehicle);

            if (bindResult.size() == 0) {
                mjVehicle.setSczt("0");
                mjVehicle.setScjg("");
                mjVehicle.setCjsj(new Date());
                String qybh = mjVehicle.getQybh();
                String dzbhs="";
                if(qybh!=null&&!qybh.equals("")){
                    List<MjDzxx> mjDzxxes = publicService.selectObjs("select * from mj_dzxx where qybh='"+qybh+"'",MjDzxx.class);
                    if(mjDzxxes.size()>0){
                        for(MjDzxx mjDzxx : mjDzxxes){
                            dzbhs+=mjDzxx.getDzbh();
                        }
                    }
                }
                mjVehicle.setDzbhs(dzbhs);
                mjVehicle.setTbdzbhs("");
                publicService.update("mj_vehicle", "xszazp,xszbzp,scqdzp,vehiclezp", "id", mjVehicle);

                publicService.insertLogInfo("编辑，成功修改【" + "车辆识别代号："
                                + mjVehicle.getClsbdh() + "】的车辆信息",
                        "编辑", "1", "车辆信息管理");
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "编辑，修改失败【" + "车辆识别代号："
                            + mjVehicle.getClsbdh() + "】的车辆信息",
                    "编辑", "0", "车辆信息管理");;
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("成功修改！") : renderError(bindResult.toString());
    }
    /**
     * 编辑用户
     *
     * @param
     * @return
     */
    @RequestMapping("/editGL")
    @ResponseBody
    public Object editGL(MjVehicle mjVehicle) {
        List<String> bindResult = new ArrayList<String>();

        try {
            bindResult = ValidateService.valid(mjVehicle);

            if (bindResult.size() == 0) {
                mjVehicle.setSczt("0");
                mjVehicle.setScjg("");
                mjVehicle.setCjsj(new Date());
                String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");
                if (mjVehicle.getXszazp()!=null&&!mjVehicle.getXszazp().equals("")) {
                    mjVehicle.setXszazp(mjVehicle.getXszazp().replace(zpurl, ""));
                }
                if (mjVehicle.getXszbzp()!=null&&!mjVehicle.getXszbzp().equals("")) {
                    mjVehicle.setXszbzp(mjVehicle.getXszbzp().replace(zpurl, ""));
                }
                if (mjVehicle.getScqdzp()!=null&&!mjVehicle.getScqdzp().equals("")) {
                    mjVehicle.setScqdzp(mjVehicle.getScqdzp().replace(zpurl, ""));
                }
                if (mjVehicle.getVehiclezp()!=null&&!mjVehicle.getVehiclezp().equals("")) {
                    mjVehicle.setVehiclezp(mjVehicle.getVehiclezp().replace(zpurl, ""));
                }
                String qybh = mjVehicle.getQybh();
                String dzbhs="";
                if(qybh!=null&&!qybh.equals("")){
                    List<MjDzxx> mjDzxxes = publicService.selectObjs("select * from mj_dzxx where qybh='"+qybh+"'",MjDzxx.class);
                    if(mjDzxxes.size()>0){
                        for(MjDzxx mjDzxx : mjDzxxes){
                            dzbhs+=mjDzxx.getDzbh();
                        }
                    }
                }
                mjVehicle.setDzbhs(dzbhs);
                mjVehicle.setTbdzbhs("");
                publicService.update("mj_vehicle", "", "id", mjVehicle);

                publicService.insertLogInfo("编辑，成功修改【" + "车辆识别代号："
                                + mjVehicle.getClsbdh() + "】的车辆信息",
                        "编辑", "1", "车辆信息管理");
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "编辑，修改失败【" + "车辆识别代号："
                            + mjVehicle.getClsbdh() + "】的车辆信息",
                    "编辑", "0", "车辆信息管理");;
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("成功修改！") : renderError(bindResult.toString());
    }
    /**
     * 编辑用户
     *
     * @param
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(MjVehicle mjVehicle) {
        List<String> bindResult = new ArrayList<String>();
        try {
            UUID uuid = UUID.randomUUID();
            String uuids = uuid.toString().replaceAll("-", "");
            mjVehicle.setId(uuids);
            bindResult = ValidateService.valid(mjVehicle);
            mjVehicle.setSczt("0");
            mjVehicle.setScjg("");
            mjVehicle.setCjsj(new Date());

            if (bindResult.size() == 0) {
                String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");
                if (mjVehicle.getXszazp()!=null&&!mjVehicle.getXszazp().equals("")) {
                    mjVehicle.setXszazp(mjVehicle.getXszazp().replace(zpurl, ""));
                }
                if (mjVehicle.getXszbzp()!=null&&!mjVehicle.getXszbzp().equals("")) {
                    mjVehicle.setXszbzp(mjVehicle.getXszbzp().replace(zpurl, ""));
                }
                if (mjVehicle.getScqdzp()!=null&&!mjVehicle.getScqdzp().equals("")) {
                    mjVehicle.setScqdzp(mjVehicle.getScqdzp().replace(zpurl, ""));
                }
                if (mjVehicle.getVehiclezp()!=null&&!mjVehicle.getVehiclezp().equals("")) {
                    mjVehicle.setVehiclezp(mjVehicle.getVehiclezp().replace(zpurl, ""));
                }
                // 验证机构编号是否已存在
                MjVehicle mjVehicle1 = publicService.selectObj("mj_vehicle", "", "cphm,cpys", mjVehicle);

                if (mjVehicle1 != null) {
                    bindResult.add("已存在，不允许重复创建");
                } else {

                    publicService.insert("mj_vehicle", "", "cphm,cpys", mjVehicle);
                    publicService.insertLogInfo("车辆管理中，点击新建，成功添加【"
                            + "车牌号码：" + mjVehicle.getCphm()
                            + "】的车辆信息", "新增", "1", "车辆管理");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "编辑，修改失败【" + "车辆识别代号："
                            + mjVehicle.getClsbdh() + "】的车辆信息",
                    "编辑", "0", "车辆信息管理");;
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("成功修改！") : renderError(bindResult.toString());
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
            MjVehicle mjVehicle = new MjVehicle();
            mjVehicle.setId(id);
            publicService.delete("mj_vehicle", "", "id", mjVehicle);
            publicService.insertLogInfo(
                    "编辑，成功修改【" + "id："
                            + mjVehicle.getId() + "】的车辆信息",
                    "删除", "1",  "车辆信息管理");
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo("删除失败，【" + "id："
                            + id+ "】的车辆信息",
                    "删除", "0",  "车辆信息管理");

            return renderError("删除异常：" + e.getMessage());
        }
    }
}

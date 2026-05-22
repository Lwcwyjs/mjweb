package com.controller.vehmanage;

import com.alibaba.fastjson.JSONObject;
import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.vehmanage.MjFdlVehicle;
import com.service.base.PublicService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
import com.service.sysmanage.SysRoleService;
import com.service.sysmanage.SysUserService;
import com.task.ExcelExportFdlTask;
import com.util.ExportProgressMonitor;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FrameGrabber;
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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description：用户管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/business/mjfdlvehicle")
public class MjFdlVehicleController extends BaseController {

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
    private final SimpleDateFormat shortDateFormater = new SimpleDateFormat("yyyy-MM-dd");


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
    public MjFdlVehicleController() {
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
            taskExecutor.submit(new ExcelExportFdlTask(
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
        return "vehmanage/MjFdlVehicle";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(MjFdlVehicle mjFdlVehicle) {
        List<String> bindResult = new ArrayList<String>();


        try {
            mjFdlVehicle.setCjsj(new Date());
            UUID uuid = UUID.randomUUID();
            String uuids = uuid.toString().replaceAll("-", "");
            mjFdlVehicle.setId(uuids);

            bindResult = ValidateService.valid(mjFdlVehicle);
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");
            if(mjFdlVehicle.getFdjmpzp()!=null) {
                mjFdlVehicle.setFdjmpzp(mjFdlVehicle.getFdjmpzp().replace(zpurl, ""));
            }
            if(mjFdlVehicle.getZcmpzp()!=null) {
                mjFdlVehicle.setZcmpzp(mjFdlVehicle.getZcmpzp().replace(zpurl, ""));
            }
            if(mjFdlVehicle.getHbbqzp()!=null) {
                mjFdlVehicle.setHbbqzp(mjFdlVehicle.getHbbqzp().replace(zpurl, ""));
            }

            if (bindResult.size() == 0) {
                mjFdlVehicle.setCjsj(new Date());
                mjFdlVehicle.setSczt("0");
                // 验证用户是否已存在
                MjFdlVehicle mjFdlVehicle1 = publicService.selectObj("mj_fdl_vehicle", "", "hbdjhm", mjFdlVehicle);
                if (mjFdlVehicle1 != null) {
                    bindResult.add("该车辆识别代号已存在，不允许重复创建");
                }
                if (bindResult.size() == 0) {

                    publicService.insert("mj_fdl_vehicle", "", "", mjFdlVehicle);

                    publicService.insertLogInfo("新建场内非道路车辆信息【" +
                                    mjFdlVehicle.getHbdjhm() + "】场内非道路车辆信息",
                            "新增", "1", "场内非道路车辆信息");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "新建场内非道路车辆信息失败【" + mjFdlVehicle.getHbdjhm() + "】场内非道路车辆信息",
                    "新增", "0", "场内非道路车辆信息");
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("新建成功！") : renderError(bindResult.toString());
    }

    @RequestMapping(value = "/dataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object dataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        String strWhere = "";

        if (params.containsKey("qybh") && params.get("qybh").toString().equals("")) {
            String organ = getCurrentUserOrganCode();
            String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = params.get("qybh").toString() + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", organ);
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
        if (params.containsKey("hbdjhm") && StringUtils.isNoneBlank(params.get("hbdjhm").toString())) {
            strWhere += " and hbdjhm like '%" + params.get("hbdjhm").toString() + "%'";
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and cjsj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and cjsj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        String field = "*";
        publicService.selectPageObjects(pageInfo, "mj_fdl_vehicle", field, strWhere, "cjsj");
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
        return "vehmanage/MjFdlVehicleEdit";
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
        MjFdlVehicle mjFdlVehicle = new MjFdlVehicle();
        mjFdlVehicle.setId(id);
        mjFdlVehicle = publicService.selectObj("mj_fdl_vehicle", "", "id", mjFdlVehicle);
        String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
        if(mjFdlVehicle.getFdjmpzp()!=null&&!mjFdlVehicle.getFdjmpzp().equals("")){
            mjFdlVehicle.setFdjmpzp(photourl+mjFdlVehicle.getFdjmpzp());
        }
        if(mjFdlVehicle.getZcmpzp()!=null&&!mjFdlVehicle.getZcmpzp().equals("")){
            mjFdlVehicle.setZcmpzp(photourl+mjFdlVehicle.getZcmpzp());
        }
        if(mjFdlVehicle.getHbbqzp()!=null&&!mjFdlVehicle.getHbbqzp().equals("")){
            mjFdlVehicle.setHbbqzp(photourl+mjFdlVehicle.getHbbqzp());
        }
        request.setAttribute("mjFdlVehicle", mjFdlVehicle);
        String organname = sysOrganizationService.getQyjcByQybh(mjFdlVehicle.getQybh());
        request.setAttribute("organname", organname);
        request.setAttribute("oper", "edit");

        return "vehmanage/MjFdlVehicleEdit";
    }
    @RequestMapping(value="/sendzcmpzp", method = RequestMethod.POST)
    @ResponseBody
    public String sendzcmpzp(@RequestParam("file") MultipartFile file) {
        String srt = "{\"code\":\"0\",\"message\":\"上传失败\"}";
        try {
            logger.info("sendzcmpzp");
            // 图片路径
            String imgUrl = null;
            String uploadDir = sysOptionService.getConfigValue("系统设置", "照片存储路径");
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");

            String sdate = shortDateFormater.format(new Date());
            String syear = sdate.substring(0, 4);
            String smonth = sdate.substring(5, 7);
            String sday = sdate.substring(8, 10);
            String remotepath = "zcmp/" + syear + "/" + smonth + "/" + sday;
            String zpdir = uploadDir + "/zcmp/" + syear + "/" + smonth + "/" + sday;
            File dir = new File(zpdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String ofilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString().replace("-", "") + ofilename.substring(ofilename.lastIndexOf("."));
            String remotefilename = "/zcmp/" + syear + "/" + smonth + "/" + sday + "/" + filename;
            zpurl = zpurl + remotefilename;
            filename = zpdir + "/" + filename;
            //上传
            if (upload(file, filename)) {
                srt = "{\"code\":\"1\",\"message\":\""+zpurl+"\"}";
            }
            else{
                srt = "{\"code\":\"0\",\"message\":\"保存失败\"}";
            }

        } catch (
                Exception e) {
            e.printStackTrace();
            srt = "{\"code\":\"0\",\"message\":\""+e.getMessage()+"\"}";
            logger.info("保存行驶证失败:" + e.getMessage());
        }
        return srt;
    }
    @RequestMapping(value = "/sendfdjmpzp", method = RequestMethod.POST)
    @ResponseBody
    public String sendfdjmpzp(@RequestParam("file") MultipartFile file) {
        String srt = "{\"code\":\"0\",\"message\":\"上传失败\"}";
        try {
            logger.info("sendfdjmpzp");
            // 图片路径
            String imgUrl = null;
            String uploadDir = sysOptionService.getConfigValue("系统设置", "照片存储路径");
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");

            String sdate = shortDateFormater.format(new Date());
            String syear = sdate.substring(0, 4);
            String smonth = sdate.substring(5, 7);
            String sday = sdate.substring(8, 10);
            String remotepath = "fdjmp/" + syear + "/" + smonth + "/" + sday;
            String zpdir = uploadDir + "/fdjmp/" + syear + "/" + smonth + "/" + sday;
            File dir = new File(zpdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String ofilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString().replace("-", "") + ofilename.substring(ofilename.lastIndexOf("."));
            String remotefilename = "/fdjmp/" + syear + "/" + smonth + "/" + sday + "/" + filename;
            zpurl = zpurl + remotefilename;
            filename = zpdir + "/" + filename;
            //上传
            if (upload(file, filename)) {
                srt = "{\"code\":\"1\",\"message\":\""+zpurl+"\"}";
            }
            else{
                srt = "{\"code\":\"0\",\"message\":\"保存失败\"}";
            }

        } catch (
                Exception e) {
            e.printStackTrace();
            srt = "{\"code\":\"0\",\"message\":\""+e.getMessage()+"\"}";
            logger.info("保存行驶证失败:" + e.getMessage());
        }
        return srt;
    }
    @RequestMapping(value = "/sendhbbqzp", method = RequestMethod.POST)
    @ResponseBody
    public Object sendhbbqzp(@RequestParam("file") MultipartFile file) {
        String srt = "{\"code\":\"0\",\"message\":\"上传失败\"}";
        try {
            logger.info("sendhbbqzp");
            // 图片路径
            String imgUrl = null;
            String uploadDir = sysOptionService.getConfigValue("系统设置", "照片存储路径");
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");

            String sdate = shortDateFormater.format(new Date());
            String syear = sdate.substring(0, 4);
            String smonth = sdate.substring(5, 7);
            String sday = sdate.substring(8, 10);
            String remotepath = "hbbq/" + syear + "/" + smonth + "/" + sday;
            String zpdir = uploadDir + "/hbbq/" + syear + "/" + smonth + "/" + sday;
            File dir = new File(zpdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String ofilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString().replace("-", "") + ofilename.substring(ofilename.lastIndexOf("."));
            String remotefilename = "/hbbq/" + syear + "/" + smonth + "/" + sday + "/" + filename;
            zpurl = zpurl + remotefilename;
            filename = zpdir + "/" + filename;
            //上传
            if (upload(file, filename)) {
                srt = "{\"code\":\"1\",\"message\":\""+zpurl+"\"}";
            }
            else{
                srt = "{\"code\":\"0\",\"message\":\"保存失败\"}";
            }

        } catch (
                Exception e) {
            e.printStackTrace();
            srt = "{\"code\":\"0\",\"message\":\""+e.getMessage()+"\"}";
            logger.info("保存行驶证失败:" + e.getMessage());
        }
        return srt;
    }
    public boolean upload(MultipartFile file, String fileName) throws Exception {
        logger.info("保存照片:" + fileName);
        boolean brt = false;
        try {
            File dest = new File(fileName);
            // 判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdir();
            }
            // 保存文件
            file.transferTo(dest);
            logger.info("保存照片成功");
            brt = true;
        } catch (FrameGrabber.Exception e) {
            logger.info("保存文件异常11:" + e.getMessage());
            logger.error("保存文件异常11:" + e.getMessage());
        }
        return true;
    }

    /**
     * 编辑用户
     *
     * @param
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(MjFdlVehicle mjFdlVehicle) {
        List<String> bindResult = new ArrayList<String>();

        try {
            bindResult = ValidateService.valid(mjFdlVehicle);
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");
            if(mjFdlVehicle.getFdjmpzp()!=null) {
                mjFdlVehicle.setFdjmpzp(mjFdlVehicle.getFdjmpzp().replace(zpurl, ""));
            }
            if(mjFdlVehicle.getZcmpzp()!=null) {
                mjFdlVehicle.setZcmpzp(mjFdlVehicle.getZcmpzp().replace(zpurl, ""));
            }
            if(mjFdlVehicle.getHbbqzp()!=null) {
                mjFdlVehicle.setHbbqzp(mjFdlVehicle.getHbbqzp().replace(zpurl, ""));
            }

            if (bindResult.size() == 0) {
                mjFdlVehicle.setSczt("0");
                mjFdlVehicle.setCjsj(new Date());
                publicService.update("mj_fdl_vehicle", "", "id", mjFdlVehicle);

                publicService.insertLogInfo("编辑，成功修改【" + "车辆识别代号："
                                + mjFdlVehicle.getHbdjhm() + "】的场内非道路车辆信息",
                        "编辑", "1", "场内非道路车辆信息管理");
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "编辑，修改失败【" + "车辆识别代号："
                            + mjFdlVehicle.getHbdjhm() + "】的场内非道路车辆信息",
                    "编辑", "0", "场内非道路车辆信息管理");;
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
            MjFdlVehicle mjFdlVehicle = new MjFdlVehicle();
            mjFdlVehicle.setId(id);
            publicService.delete("mj_fdl_vehicle", "", "id", mjFdlVehicle);
            publicService.insertLogInfo(
                    "编辑，成功修改【" + "id："
                            + mjFdlVehicle.getId() + "】的场内非道路车辆信息",
                    "删除", "1",  "场内非道路车辆信息管理");
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo("删除失败，【" + "id："
                            + id+ "】的场内非道路车辆信息",
                    "删除", "0",  "场内非道路车辆信息管理");

            return renderError("删除异常：" + e.getMessage());
        }
    }
}

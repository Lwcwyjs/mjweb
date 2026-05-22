package com.controller.businessmange;

import com.alibaba.fastjson.JSONObject;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.commons.utils.TimeUtil;
import com.model.businessmange.MjDataBase;
import com.model.businessmange.MjDataDztz;
import com.model.businessmange.MjYshw;
import com.model.vehmanage.MjVehicle;
import com.service.base.PublicService;
import com.service.configmanage.SysCodeService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
import com.task.ExcelExportDztzTask;
import com.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
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
 * @description：ai识别结果查看
 */
@Controller
@RequestMapping("/business/dztz")
public class DztzController extends BaseController {

    @Autowired
    public PublicService publicService;

    @Autowired
    public SysOrganizationService sysOrganizationService;

    @Autowired
    public SysOptionService sysOptionService;
    @Autowired
    public SysCodeService sysCodeService;
    // Excel临时存储目录（需提前创建并配置）
    private static final String EXPORT_DIR = "D:/opt/temp-excel/";
    // 直接创建线程池，不依赖 Spring 注入
    private final ThreadPoolTaskExecutor taskExecutor;

    // 构造函数中初始化线程池
    public DztzController() {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("export-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize(); // 必须调用此方法启动线程池
    }

    @RequestMapping("/main")
    public String main() {
        return "businessmanage/DztzManager";
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

    @RequestMapping(value = "/DataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object getDataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
        try {
            PageInfo pageInfo = new PageInfo(page, rows, sort, order);
            String strWhere = "";
            if (params.containsKey("pfbz") && StringUtils.isNoneBlank(params.get("pfbz").toString())) {
                strWhere += String.format(" and pfbz='%s'", params.get("pfbz").toString());
            }
            if (params.containsKey("organ") && params.get("organ").toString().equals("")) {
                String organ = getCurrentUserOrganCode();
                String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = params.get("organ").toString() + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and qybh in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and qybh='%s'", organ);
                }
            } else if (params.containsKey("organ") && (StringUtils.isNoneBlank(params.get("organ").toString()))) {
                String organ = params.get("organ").toString();
                String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = organ + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and qybh in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and qybh='%s'", organ);
                }
            }
            if (params.containsKey("cphm") && StringUtils.isNoneBlank(params.get("cphm").toString())) {
                strWhere += " and cphm like '%" + params.get("cphm").toString() + "%'";
            }
            if (params.containsKey("cpys") && StringUtils.isNoneBlank(params.get("cpys").toString())) {
                String spzl = params.get("cpys").toString();
                strWhere += String.format(" and cpys='%s'", spzl);
            }
            if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and jcsj  > '%s 00:00:00'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and jcsj  <= '%s 23:59:59'", params.get("jssj").toString());
            }
            String field = "*";
            publicService.selectPageObjects(pageInfo, "mj_data_dztz", field, strWhere, "jcsj desc");
            return pageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return renderError(e.getMessage());
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
            taskExecutor.submit(new ExcelExportDztzTask(
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
    @RequestMapping(value = "/getExcel", method = RequestMethod.GET)
    public void getExcel(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort,
                         String order, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("开始导出电子台账excel");
        ExcelExportUtil util = new ExcelExportUtil(ExcelType.XLS);
        List<ExcelCell> list = new ArrayList<ExcelCell>();
        String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
        String strWhere = " 1=1 ";
        if (params.containsKey("pfbz") && StringUtils.isNoneBlank(params.get("pfbz").toString())) {
            strWhere += String.format(" and pfbz='%s'", params.get("pfbz").toString());
        }
        if (params.containsKey("organ") && params.get("organ").toString().equals("")) {
            String organ = getCurrentUserOrganCode();
            String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = params.get("organ").toString() + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", organ);
            }
        } else if (params.containsKey("organ") && (StringUtils.isNoneBlank(params.get("organ").toString()))) {
            String organ = params.get("organ").toString();
            String strOrgans = sysOrganizationService.findChildrenCodes(organ);
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = organ + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", organ);
            }
        }

        if (params.containsKey("cphm") && StringUtils.isNoneBlank(params.get("cphm").toString())) {
            strWhere += " and cphm like '%" + params.get("cphm").toString() + "%'";
        }
        if (params.containsKey("cpys") && StringUtils.isNoneBlank(params.get("cpys").toString())) {
            String spzl = params.get("cpys").toString();
            strWhere += String.format(" and cpys='%s'", spzl);
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and jcsj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and jcsj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        SimpleDateFormat sdfsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            //String sql="select * from mj_data_dztz where "+strWhere+" order by jcsj";
            String sql="select jccrkbh,jcdzbh,cccrkbh,jcsj,ccsj,cphm,cpys,d.oi_value as cllx,ccdjrq,clsbdh,clppxh,rlzl," +
                    "pfbz,lwzt,syxz,jcyshwmc,jcysl,ccyshwmc,ccysl,syr,cczp,jczp,cczp,scqdzp,xszzp from mj_data_dztz c " +
                    "left join ai_sys_code d on c.cllx=d.oi_code and d.oi_name='车辆类型' where "+strWhere+" order by jcsj";
            List<MjDataDztz>  mjDataDztzs= publicService.selectObjs(sql,MjDataDztz.class);

            int row = 0;
            int col = 0;
            list.add(new ExcelCell(row, col++, "进厂出入口编号"));
            list.add(new ExcelCell(row, col++, "进厂道闸编号"));
            list.add(new ExcelCell(row, col++, "出厂出入口编号"));
            list.add(new ExcelCell(row, col++, "出厂道闸编号"));
            list.add(new ExcelCell(row, col++, "进厂时间"));
            list.add(new ExcelCell(row, col++, "出厂时间"));
            list.add(new ExcelCell(row, col++, "车牌号码"));
            list.add(new ExcelCell(row, col++, "号牌颜色"));
            list.add(new ExcelCell(row, col++, "车辆类型"));
            list.add(new ExcelCell(row, col++, "注册登记日期"));
            list.add(new ExcelCell(row, col++, "车辆识别代号(VIN)"));
            list.add(new ExcelCell(row, col++, "车辆品牌型号"));
            list.add(new ExcelCell(row, col++, "燃料类型"));
            list.add(new ExcelCell(row, col++, "排放标准"));
            list.add(new ExcelCell(row, col++, "联网状态"));
            list.add(new ExcelCell(row, col++, "使用性质"));
            list.add(new ExcelCell(row, col++, "进厂运输货物名称"));
            list.add(new ExcelCell(row, col++, "进厂运输量"));
            list.add(new ExcelCell(row, col++, "出厂运输货物名称"));
            list.add(new ExcelCell(row, col++, "出厂运输量"));
            list.add(new ExcelCell(row, col++, "车队名称"));
            list.add(new ExcelCell(row, col++, "进厂照片"));
            list.add(new ExcelCell(row, col++, "出厂照片"));
            list.add(new ExcelCell(row, col++, "随车清单"));
            list.add(new ExcelCell(row, col++, "行驶证"));
            ConverUtil converUtil=new ConverUtil();
            if (mjDataDztzs.size()>0) {
                for (MjDataDztz mjDataDztz:mjDataDztzs) {
                    row++;
                    col = 0;
                    list.add(new ExcelCell(row, col++, mjDataDztz.getJccrkbh()));
                    list.add(new ExcelCell(row, col++, mjDataDztz.getJcdzbh()));
                    list.add(new ExcelCell(row, col++, mjDataDztz.getCccrkbh()));
                    list.add(new ExcelCell(row, col++, mjDataDztz.getCcdzbh()));
                    list.add(new ExcelCell(row, col++, sdfsj.format(mjDataDztz.getJcsj())));
                    if(mjDataDztz.getCcsj()!=null) {
                        list.add(new ExcelCell(row, col++, sdfsj.format(mjDataDztz.getCcsj())));
                    }
                    else{
                        list.add(new ExcelCell(row, col++, ""));
                    }
                    list.add(new ExcelCell(row, col++, mjDataDztz.getCphm()));
                    list.add(new ExcelCell(row, col++, ConvertCpys(mjDataDztz.getCpys())));
                    list.add(new ExcelCell(row, col++, mjDataDztz.getCllx()));
                    if(mjDataDztz.getCcdjrq()!=null) {
                        list.add(new ExcelCell(row, col++, sdf.format(mjDataDztz.getCcdjrq())));
                    }
                    else{
                        list.add(new ExcelCell(row, col++, ""));
                    }
                    list.add(new ExcelCell(row, col++, mjDataDztz.getClsbdh()));
                    list.add(new ExcelCell(row, col++, mjDataDztz.getClppxh()));
                    list.add(new ExcelCell(row, col++, converUtil.convertrlzlex(mjDataDztz.getRlzl())));
                    list.add(new ExcelCell(row, col++, converUtil.pfsztopfhz(mjDataDztz.getPfbz())));
                    list.add(new ExcelCell(row, col++, converUtil.convertlwzt(mjDataDztz.getLwzt())));
                    list.add(new ExcelCell(row, col++, ConvertSyxz(mjDataDztz.getSyxz())));
                    list.add(new ExcelCell(row, col++, mjDataDztz.getJcyshwmc()));
                    list.add(new ExcelCell(row, col++, mjDataDztz.getJcysl()));
                    list.add(new ExcelCell(row, col++, mjDataDztz.getCcyshwmc()));
                    list.add(new ExcelCell(row, col++, mjDataDztz.getCcysl()));
                    list.add(new ExcelCell(row, col++, mjDataDztz.getSyr()));
                    if(mjDataDztz.getJczp()!=null&&!mjDataDztz.getJczp().equals("")){
                        list.add(new ExcelCell(row, col++, photourl+mjDataDztz.getJczp()));
                    }
                    else{
                        list.add(new ExcelCell(row, col++, mjDataDztz.getJczp()));
                    }
                    if(mjDataDztz.getCczp()!=null&&!mjDataDztz.getCczp().equals("")){
                        list.add(new ExcelCell(row, col++, photourl+mjDataDztz.getCczp()));
                    }
                    else{
                        list.add(new ExcelCell(row, col++, mjDataDztz.getCczp()));
                    }
                    if(mjDataDztz.getScqdzp()!=null&&!mjDataDztz.getScqdzp().equals("")){
                        list.add(new ExcelCell(row, col++,photourl+mjDataDztz.getScqdzp()));
                    }
                    else{
                        list.add(new ExcelCell(row, col++, mjDataDztz.getScqdzp()));
                    }
                    if(mjDataDztz.getXszzp()!=null&&!mjDataDztz.getXszzp().equals("")){
                        list.add(new ExcelCell(row, col++,photourl+mjDataDztz.getXszzp()));
                    }
                    else{
                        list.add(new ExcelCell(row, col++, mjDataDztz.getXszzp()));
                    }
                }
            }
            Workbook book = util.export(list);
            response.setContentType("application/vnd.ms-excel");
            TimeUtil tm=new TimeUtil();
            String fileName=tm.getRandomFileName()+"电子台账";
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
            ServletOutputStream out = response.getOutputStream();
            book.write(out);
            out.close();
            logger.info("导出电子台账excel成功");
        }catch(Exception e){
            logger.error("导出错误:"+e.getMessage());
            //logger.error(e.getMessage());
            util.createTitle(0, e.getMessage(), 0, 0);
            Workbook book = util.export(list);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=error.xls");
            ServletOutputStream out = response.getOutputStream();
            book.write(out);
            out.close();
        }
        logger.info("导出电子台账excel结束");
    }

    public static String ConvertCpys(String cpys)
    {
        if(cpys==null){
            cpys="";
        }
        String srt = "";
        if (cpys.equals("0"))
        {
            srt = "蓝色";
        }
        else if (cpys.equals("1"))
        {
            srt = "黄色";
        }
        else if (cpys.equals("2"))
        {
            srt = "白色";
        }
        else if (cpys.equals("3"))
        {
            srt = "黑色";
        }
        else if (cpys.equals("4"))
        {
            srt = "绿色";
        }
        else if (cpys.equals("6"))
        {
            srt = "黄绿色";
        }
        else
        {
            srt = "其他";
        }
        return srt;
    }
    public static String ConvertSyxz(String syxz)
    {
        if(syxz==null){
            syxz="";
        }
        String srt = "";
        if (syxz.equals("A"))
        {
            srt = "非营运";
        }
        else if (syxz.equals("B"))
        {
            srt = "公路客运";
        }
        else if (syxz.equals("C"))
        {
            srt = "公交客运";
        }
        else if (syxz.equals("D"))
        {
            srt = "出租客运";
        }
        else if (syxz.equals("E"))
        {
            srt = "旅游客运";
        }
        else if (syxz.equals("F"))
        {
            srt = "货运";
        }
        else if (syxz.equals("H"))
        {
            srt = "警用";
        }
        else if (syxz.equals("I"))
        {
            srt = "消防";
        }
        else if (syxz.equals("J"))
        {
            srt = "救护";
        }
        else if (syxz.equals("K"))
        {
            srt = "工程抢险";
        }
        else if (syxz.equals("L"))
        {
            srt = "营转非";
        }
        else if (syxz.equals("M"))
        {
            srt = "出租转非";
        }
        else if (syxz.equals("N"))
        {
            srt = "教练";
        }
        else if (syxz.equals("O"))
        {
            srt = "幼儿校车";
        }
        else if (syxz.equals("P"))
        {
            srt = "小学生校车";
        }
        else if (syxz.equals("Q"))
        {
            srt = "初中生校车";
        }
        else if (syxz.equals("R"))
        {
            srt = "危险品运输";
        }
        else if (syxz.equals("S"))
        {
            srt = "中小学生校车";
        }
        return srt;
    }

    /**
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/showPage")
    public String showPage(String id, String type, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        MjDataDztz mjDataDztz = new MjDataDztz();
        mjDataDztz.setId(id);
        mjDataDztz = publicService.selectObj("mj_data_dztz", "", "id", mjDataDztz);
        String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
        String cphm=mjDataDztz.getCphm();
        String cpys=mjDataDztz.getCpys();
        if(mjDataDztz.getJczp()!=null&&!mjDataDztz.getJczp().equals("")){
            mjDataDztz.setJczp(photourl+mjDataDztz.getJczp());
        }
        if(mjDataDztz.getCczp()!=null&&!mjDataDztz.getCczp().equals("")){
            mjDataDztz.setCczp(photourl+mjDataDztz.getCczp());
        }
        if(mjDataDztz.getXszzp()!=null&&!mjDataDztz.getXszzp().equals("")){
            mjDataDztz.setXszzp(photourl+mjDataDztz.getXszzp());
        }
        if(mjDataDztz.getScqdzp()!=null&&!mjDataDztz.getScqdzp().equals("")){
            mjDataDztz.setScqdzp(photourl+mjDataDztz.getScqdzp());
        }
        if(mjDataDztz.getCpys()!=null&&!mjDataDztz.getCpys().equals("")){
            mjDataDztz.setCpys(sysCodeService.getValueByCode("车牌颜色",mjDataDztz.getCpys()));
        }
        if(mjDataDztz.getPfbz()!=null&&!mjDataDztz.getPfbz().equals("")){
            mjDataDztz.setPfbz(sysCodeService.getValueByCode("排放标准",mjDataDztz.getPfbz()));
        }
        if(mjDataDztz.getRlzl()!=null&&!mjDataDztz.getRlzl().equals("")){
            mjDataDztz.setRlzl(sysCodeService.getValueByCode("燃料种类",mjDataDztz.getRlzl()));
        }
        if(mjDataDztz.getCllx()!=null&&!mjDataDztz.getCllx().equals("")){
            mjDataDztz.setCllx(sysCodeService.getValueByCode("车辆类型",mjDataDztz.getCllx()));
        }
        if(mjDataDztz.getSyxz()!=null&&!mjDataDztz.getSyxz().equals("")){
            mjDataDztz.setSyxz(sysCodeService.getValueByCode("使用性质",mjDataDztz.getSyxz()));
        }
        if(mjDataDztz.getQybh()!=null&&!mjDataDztz.getQybh().equals("")){
            mjDataDztz.setQybh(sysOrganizationService.getQymcByQybh(mjDataDztz.getQybh()));
        }
        if(mjDataDztz.getLwzt()!=null&&!mjDataDztz.getLwzt().equals("")){
            String lwzt=mjDataDztz.getLwzt();
            if(lwzt.equals("1")){
                lwzt="联网";
            }
            else{
                lwzt="未联网";
            }
            mjDataDztz.setLwzt(lwzt);
        }
        MjVehicle mjVehicle=new MjVehicle();
        mjVehicle.setCphm(cphm);
        mjVehicle.setCpys(cpys);
        mjVehicle = publicService.selectObj("mj_vehicle", "", "cphm,cpys", mjVehicle);
        if(mjVehicle!=null){
            if(mjDataDztz.getSyr()!=null&&!mjDataDztz.getSyr().equals("")){
                mjDataDztz.setSyr(mjVehicle.getSyr());
            }
        }
        request.setAttribute("mjDataDztz", mjDataDztz);

        return "businessmanage/DztzView";
    }

    @RequestMapping("/editPage")
    public String editPage(String id, String type, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        MjDataDztz mjDataDztz = new MjDataDztz();
        mjDataDztz.setId(id);
        mjDataDztz = publicService.selectObj("mj_data_dztz", "", "id", mjDataDztz);
        request.setAttribute("mjDataDztz", mjDataDztz);
        return "businessmanage/DztzEdit";
    }

    @RequestMapping(value = "/edityshw")
    @ResponseBody
    public Object edityshw(MjDataDztz mjDataDztz) {
        List<String> bindResult = new ArrayList<String>();

        try {
            publicService.update("update mj_data_dztz set jcyshwmc='"+mjDataDztz.getJcyshwmc()+"',jcysl='"+mjDataDztz.getJcysl()+"',jcysdw='"+mjDataDztz.getJcysdw()+
                    "',ccyshwmc='"+mjDataDztz.getCcyshwmc()+"',ccysl='"+mjDataDztz.getCcysl()+"',ccysdw='"+mjDataDztz.getCcysdw()+"',syr='"+mjDataDztz.getSyr()+"' where id='"+mjDataDztz.getId()+"'");
            MjDataDztz mjDataDztz1=new MjDataDztz();
            mjDataDztz1=publicService.selectObj("mj_data_dztz","","id",mjDataDztz);
            if(mjDataDztz1!=null) {
                publicService.update("update mj_data_base set yshwmc='"+mjDataDztz.getJcyshwmc()+"',ysl='"+mjDataDztz.getJcysl()+"',ysdw='"+mjDataDztz.getJcysdw()+
                        "',syr='"+mjDataDztz.getSyr()+"',sczt='0',scjg='' where id='"+mjDataDztz1.getJcid()+"'");
                if(mjDataDztz1.getJcid()!=null) {
                    MjYshw mjYshw = new MjYshw();
                    mjYshw.setId(mjDataDztz1.getJcid());
                    MjDataBase mjDataBase = new MjDataBase();
                    mjDataBase.setId(mjDataDztz1.getJcid());
                    mjDataBase = publicService.selectObj("mj_data_base", "", "id", mjDataBase);
                    if (mjDataBase != null) {
                        mjYshw.setId(mjDataBase.getId());
                        mjYshw.setLsh(mjDataBase.getLsh());
                        mjYshw.setYshwmc(mjDataBase.getYshwmc());
                        mjYshw.setYsl(mjDataBase.getYsl());
                        mjYshw.setYsdw(mjDataBase.getYsdw());
                        mjYshw.setSyr(mjDataBase.getSyr());
                        mjYshw.setSczt("0");
                        mjYshw.setScjg("");
                        mjYshw.setJclx(mjDataBase.getJclx());
                        mjYshw.setCphm(mjDataBase.getCphm());
                        mjYshw.setQybh(mjDataBase.getQybh());
                        mjYshw.setTgkssj(mjDataBase.getTgkssj());
                        mjYshw.setCpys(mjDataBase.getCpys());
                        mjYshw.setDzbh(mjDataBase.getDzbh());
                        MjYshw mjYshw1 = publicService.selectObj("mj_yshw", "", "id", mjYshw);
                        if (mjYshw1 != null) {
                            publicService.update("mj_yshw", "id", "id", mjYshw);
                        } else {
                            publicService.insert("mj_yshw", "", "id", mjYshw);
                        }
                    }
                }
                publicService.update("update mj_data_base set yshwmc='"+mjDataDztz.getCcyshwmc()+"',ysl='"+mjDataDztz.getCcysl()+"',ysdw='"+mjDataDztz.getCcysdw()+
                        "',syr='"+mjDataDztz.getSyr()+"',sczt='0',scjg='' where id='"+mjDataDztz1.getCcid()+"'");
                if(mjDataDztz1.getCcid()!=null) {
                    MjYshw mjYshw = new MjYshw();
                    mjYshw.setId(mjDataDztz1.getJcid());
                    MjDataBase mjDataBase = new MjDataBase();
                    mjDataBase.setId(mjDataDztz1.getCcid());
                    mjDataBase = publicService.selectObj("mj_data_base", "", "id", mjDataBase);
                    if (mjDataBase != null) {
                        mjYshw.setId(mjDataBase.getId());
                        mjYshw.setLsh(mjDataBase.getLsh());
                        mjYshw.setYshwmc(mjDataBase.getYshwmc());
                        mjYshw.setYsl(mjDataBase.getYsl());
                        mjYshw.setYsdw(mjDataBase.getYsdw());
                        mjYshw.setSyr(mjDataBase.getSyr());
                        mjYshw.setSczt("0");
                        mjYshw.setScjg("");
                        mjYshw.setJclx(mjDataBase.getJclx());
                        mjYshw.setCphm(mjDataBase.getCphm());
                        mjYshw.setQybh(mjDataBase.getQybh());
                        mjYshw.setTgkssj(mjDataBase.getTgkssj());
                        mjYshw.setCpys(mjDataBase.getCpys());
                        mjYshw.setDzbh(mjDataBase.getDzbh());
                        MjYshw mjYshw1 = publicService.selectObj("mj_yshw", "", "id", mjYshw);
                        if (mjYshw1 != null) {
                            publicService.update("mj_yshw", "id", "id", mjYshw);
                        } else {
                            publicService.insert("mj_yshw", "", "id", mjYshw);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("编辑成功！") : renderError(bindResult.toString());
    }

}

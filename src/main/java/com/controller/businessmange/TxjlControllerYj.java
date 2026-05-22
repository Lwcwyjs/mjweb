package com.controller.businessmange;

import com.alibaba.fastjson.JSONObject;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.businessmange.MjDataBase;
import com.model.businessmange.MjDataVideo;
import com.model.businessmange.MjYshw;
import com.model.videomanage.MjSpxx;
import com.service.base.PublicService;
import com.service.configmanage.SysCodeService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
import com.task.ExcelExportTask;
import com.util.ExportProgressMonitor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description：ai识别结果查看
 */
@Controller
@RequestMapping("/business/txjlYj")
public class TxjlControllerYj extends BaseController {

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
    public TxjlControllerYj() {
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
            params.put("bgzt", "1");
            // 存储导出参数
            ExportProgressMonitor.getInstance().setParams(taskId, params);
            // 启动异步导出任务
            taskExecutor.submit(new ExcelExportTask(
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
    @RequestMapping("/main")
    public String main() {
        return "businessmanage/TxjlManagerYj";
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
                if(organ.equals("1306020010")){
                    strWhere += " and (qybh='1306020010' or qybh='1306020001' or qybh='1733227357')";
                }
                else {
                    String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
                    if (StringUtils.isNoneBlank(strOrgans)) {
                        strOrgans = params.get("organ").toString() + "," + strOrgans;
                        strOrgans = strOrgans.replace(",", "','");
                        strWhere += String.format(" and qybh in ('%s')", strOrgans);
                    } else {
                        strWhere += String.format(" and qybh='%s'", organ);
                    }
                }
            } else if (params.containsKey("organ") && (StringUtils.isNoneBlank(params.get("organ").toString()))) {
                String organ = params.get("organ").toString();
                if(organ.equals("1306020010")){
                    strWhere += " and (qybh='1306020010' or qybh='1306020001' or qybh='1733227357')";
                }
                else {
                    String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                    if (StringUtils.isNoneBlank(strOrgans)) {
                        strOrgans = organ + "," + strOrgans;
                        strOrgans = strOrgans.replace(",", "','");
                        strWhere += String.format(" and qybh in ('%s')", strOrgans);
                    } else {
                        strWhere += String.format(" and qybh='%s'", organ);
                    }
                }
            }
            if (params.containsKey("cphm") && StringUtils.isNoneBlank(params.get("cphm").toString())) {
                strWhere += " and cphm like '%" + params.get("cphm").toString() + "%'";
            }
            if (params.containsKey("cpys") && StringUtils.isNoneBlank(params.get("cpys").toString())) {
                String spzl = params.get("cpys").toString();
                strWhere += String.format(" and cpys='%s'", spzl);
            }
            strWhere += " and bgzt='1'";
            if (params.containsKey("jclx") && StringUtils.isNoneBlank(params.get("jclx").toString())) {
                strWhere += String.format(" and jclx='%s'", params.get("jclx").toString());
            }
            if (params.containsKey("dzbh") && StringUtils.isNoneBlank(params.get("dzbh").toString())) {
                strWhere += String.format(" and dzbh='%s'", params.get("dzbh").toString());
            }
            if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
            }
            String tblName = "(select id,a.lsh,d.name as organ,a.cphm,cpys,pfbz,a.bgzt,a.dzbh,a.tgkssj,a.tgjssj,jclx,gkjg,yshwmc " +
                    " from (select * from mj_data_base  where 1=1 " + strWhere + ")a " +
                    " left join (select * from ai_sys_organization) d on a.qybh = d.organ)a";
            logger.info(tblName);
            publicService.selectPageObjects(pageInfo, tblName, "*", "", "tgkssj desc");
            pageInfo.setCode(0);
            return pageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return renderError(e.getMessage());
        }
    }
    

    /**
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/showPage")
    public String showPage(String id, String type, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        MjDataBase mjDataBase = new MjDataBase();
        mjDataBase.setId(id);
        mjDataBase = publicService.selectObj("mj_data_base", "", "id", mjDataBase);
        String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
        MjSpxx mjSpxx=new MjSpxx();
        mjSpxx.setQybh(mjDataBase.getQybh());
        mjSpxx.setDzbh(mjDataBase.getDzbh());
        mjSpxx.setJclx(mjDataBase.getJclx());
        mjSpxx=publicService.selectObj("mj_spxx","","qybh,dzbh,jclx",mjSpxx);
        if(mjDataBase.getCpzp()!=null&&!mjDataBase.getCpzp().equals("")){
            mjDataBase.setCpzp(photourl+mjDataBase.getCpzp());
        }
        if(mjDataBase.getCtzp()!=null&&!mjDataBase.getCtzp().equals("")){
            mjDataBase.setCtzp(photourl+mjDataBase.getCtzp());
        }
        if(mjDataBase.getCszp()!=null&&!mjDataBase.getCszp().equals("")){
            mjDataBase.setCszp(photourl+mjDataBase.getCszp());
        }
        if(mjDataBase.getCpys()!=null&&!mjDataBase.getCpys().equals("")){
            mjDataBase.setCpys(sysCodeService.getValueByCode("车牌颜色",mjDataBase.getCpys()));
        }
        if(mjDataBase.getPfbz()!=null&&!mjDataBase.getPfbz().equals("")){
            mjDataBase.setPfbz(sysCodeService.getValueByCode("排放标准",mjDataBase.getPfbz()));
        }
        if(mjDataBase.getRlzl()!=null&&!mjDataBase.getRlzl().equals("")){
            mjDataBase.setRlzl(sysCodeService.getValueByCode("燃料种类",mjDataBase.getRlzl()));
        }
        if(mjDataBase.getBgzt()!=null&&!mjDataBase.getBgzt().equals("")){
            mjDataBase.setBgzt(sysCodeService.getBgztbyCode(mjDataBase.getBgzt()));
        }
        if(mjDataBase.getJclx()!=null&&!mjDataBase.getJclx().equals("")){
            mjDataBase.setJclx(sysCodeService.getJclxbyCode(mjDataBase.getJclx()));
        }
        if(mjDataBase.getQybh()!=null&&!mjDataBase.getQybh().equals("")){
            mjDataBase.setQybh(sysOrganizationService.getQymcByQybh(mjDataBase.getQybh()));
        }

        String videortsp="";
        Date tgkssj=mjDataBase.getTgkssj();
        Date tgjssj=mjDataBase.getTgjssj();
        SimpleDateFormat sdfsj = new SimpleDateFormat("yyyyMMddHHmmss");
        if(tgkssj!=null){
            long millis = tgkssj.getTime();
            // 加15秒（1秒 = 1000毫秒）
            long millisAfter15Seconds = millis -5 * 1000;
            // 将加秒后的毫秒数转换回Date对象
            tgkssj = new Date(millisAfter15Seconds);
        }
        String stgkssj= sdfsj.format(tgkssj);
        stgkssj=stgkssj.substring(0,8)+"t"+stgkssj.substring(8)+"z";
        if (tgjssj==null){
            long millis = tgkssj.getTime();
            // 加15秒（1秒 = 1000毫秒）
            long millisAfter15Seconds = millis + 20 * 1000;
            // 将加秒后的毫秒数转换回Date对象
            tgjssj = new Date(millisAfter15Seconds);
        }
        else{
            long millis = tgjssj.getTime();
            // 加15秒（1秒 = 1000毫秒）
            long millisAfter15Seconds = millis + 5 * 1000;
            // 将加秒后的毫秒数转换回Date对象
            tgjssj = new Date(millisAfter15Seconds);
        }
        String stgjssj=sdfsj.format(tgjssj);
        stgjssj=stgjssj.substring(0,8)+"t"+stgjssj.substring(8)+"z";
        if(mjSpxx!=null){
            videortsp=mjSpxx.getReplayurl();
            videortsp=videortsp+"?starttime="+stgkssj+"&endtime="+stgjssj;
        }
        request.setAttribute("mjDataBase", mjDataBase);
        request.setAttribute("videortsp", videortsp);
        return "businessmanage/TxjlView";
    }

    @RequestMapping(value = "/videoPlay", method = RequestMethod.GET)
    public String videoPlay(String url, HttpServletRequest request) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
            request.setAttribute("url", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "businessmanage/videoPlay";
    }
    @RequestMapping(value = "/playvlc", method = RequestMethod.POST)
    @ResponseBody
    public Object playvlc(HttpServletRequest request) throws IOException, InterruptedException {
        Map<String, Object> data = new HashMap<String, Object>();
        String rtspUrl = request.getParameter("videortsp");
        MjDataVideo mjDataVideo=new MjDataVideo();
        mjDataVideo.setBaseid(request.getParameter("id"));
        mjDataVideo=publicService.selectObj("mj_data_video","","baseid",mjDataVideo);
        if(mjDataVideo!=null){
            data.put("m3u8Url", "http://27.129.145.98:18082"+mjDataVideo.getPtlj());
        }
        else {
            data.put("m3u8Url", rtspUrl);
        }
        return data;
    }
    @RequestMapping("/editPage")
    public String editPage(String id, String type, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        MjDataBase mjDataBase = new MjDataBase();
        mjDataBase.setId(id);
        mjDataBase = publicService.selectObj("mj_data_base", "", "id", mjDataBase);
        request.setAttribute("mjDataBase", mjDataBase);
        return "businessmanage/TxjlEdit";
    }

    @RequestMapping(value = "/edityshw")
    @ResponseBody
    public Object edityshw(MjDataBase mjDataBase) {
        List<String> bindResult = new ArrayList<String>();
        logger.debug("edityshw:"+mjDataBase.getId());
        try {
            publicService.update("update mj_data_base set yshwmc='"+mjDataBase.getYshwmc()+"',ysl='"+mjDataBase.getYsl()+"',ysdw='"+mjDataBase.getYsdw()+
                    "',syr='"+mjDataBase.getSyr()+"',sczt='0',scjg='' where id='"+mjDataBase.getId()+"'");
            MjDataBase mjDataBase1=new MjDataBase();
            mjDataBase1=publicService.selectObj("mj_data_base","","id",mjDataBase);
            if(mjDataBase1!=null){
                if(mjDataBase1.getJclx().equals("1")){
                    publicService.update("update mj_data_dztz set jcyshwmc='"+mjDataBase.getYshwmc()+"',jcysl='"+mjDataBase.getYsl()+"',jcysdw='"+mjDataBase.getYsdw()+
                            "',syr='"+mjDataBase.getSyr()+"' where jcid='"+mjDataBase.getId()+"'");
                }
                else{
                    publicService.update("update mj_data_dztz set ccyshwmc='"+mjDataBase.getYshwmc()+"',ccysl='"+mjDataBase.getYsl()+"',ccysdw='"+mjDataBase.getYsdw()+
                            "',syr='"+mjDataBase.getSyr()+"' where ccid='"+mjDataBase.getId()+"'");
                }
                logger.debug("更新成功:"+mjDataBase.getId());
                MjYshw mjYshw = new MjYshw();
                mjYshw.setId(mjDataBase.getId());
                mjYshw.setLsh(mjDataBase1.getLsh());
                mjYshw.setYshwmc(mjDataBase.getYshwmc());
                mjYshw.setYsl(mjDataBase.getYsl());
                mjYshw.setYsdw(mjDataBase.getYsdw());
                mjYshw.setSyr(mjDataBase.getSyr());
                mjYshw.setSczt("0");
                mjYshw.setScjg("");
                mjYshw.setJclx(mjDataBase1.getJclx());
                mjYshw.setCphm(mjDataBase1.getCphm());
                mjYshw.setQybh(mjDataBase1.getQybh());
                mjYshw.setTgkssj(mjDataBase1.getTgkssj());
                mjYshw.setCpys(mjDataBase1.getCpys());
                mjYshw.setDzbh(mjDataBase1.getDzbh());
                MjYshw mjYshw1 = publicService.selectObj("mj_yshw","","id",mjYshw);
                if(mjYshw1!=null){
                    logger.debug("更新mj_yshw成功:"+mjDataBase.getId());
                    publicService.update("mj_yshw","id","id",mjYshw);
                }
                else{
                    logger.debug("插入mj_yshw成功:"+mjDataBase.getId());
                    publicService.insert("mj_yshw","","id",mjYshw);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("编辑成功！") : renderError(bindResult.toString());
    }
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("流水号");
        headerRow.createCell(1).setCellValue("车牌号");

        String sql = "select top 1000 lsh, cphm from mj_data_base"; // 保持原来的SQL语句
        List<MjDataBase> mjDataBases = publicService.selectObjs(sql, MjDataBase.class);
        for (int i = 0; i < mjDataBases.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(mjDataBases.get(i).getLsh());
            row.createCell(1).setCellValue(mjDataBases.get(i).getCphm());
        }

        // 将工作簿写入输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close(); // 确保关闭工作簿

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=export.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

}

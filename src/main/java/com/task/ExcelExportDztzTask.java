package com.task;

import com.model.businessmange.MjDataDztzX;
import com.service.base.PublicService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
import com.util.ConverUtil;
import com.util.ExcelCell;
import com.util.ExcelExportUtilex;
import com.util.ExportProgressMonitor;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.controller.businessmange.DztzController.ConvertCpys;
import static com.controller.businessmange.DztzController.ConvertSyxz;

/**
 * 异步导出任务
 */
public class ExcelExportDztzTask implements Callable<Void> {
    private final String taskId;
    private final String exportDir; // Excel临时存储目录（如：/opt/temp-excel/）

    // 注入你需要的Service（根据实际项目修改，可通过构造函数传入）
    private final SysOptionService sysOptionService;
    private final PublicService publicService;
    private final SysOrganizationService sysOrganizationService;
    private final Logger logger;

    public ExcelExportDztzTask(String taskId, String exportDir,
                               SysOptionService sysOptionService,
                               PublicService publicService,
                               SysOrganizationService sysOrganizationService,
                               Logger logger) {
        this.taskId = taskId;
        this.exportDir = exportDir;
        this.sysOptionService = sysOptionService;
        this.publicService = publicService;
        this.sysOrganizationService = sysOrganizationService;
        this.logger = logger;
    }

    @Override
    public Void call() throws InterruptedException {
        // 移除异步线程中获取HttpServletRequest的代码（避免空指针）
        // 如需请求相关参数，应在提交任务前通过构造函数传入
        FileOutputStream fos = null;
        ExcelExportUtilex util = null;
        try {
            // 1. 获取导出参数（从监控器中获取，确保非空）
            Map<String, Object> params = ExportProgressMonitor.getInstance().getParams(taskId);
            if (params == null || params.isEmpty()) {
                logger.error("任务[" + taskId + "]未找到导出参数，终止导出");
                ExportProgressMonitor.getInstance().updateProgress(taskId, -2);
                return null;
            }

            // 2. 初始化进度（0%）
            ExportProgressMonitor.getInstance().updateProgress(taskId, 0);
            logger.info("任务[" + taskId + "]开始执行，初始化进度为0%");

            String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
            String strWhere = " 1=1 ";
            if (params.containsKey("pfbz") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("pfbz").toString())) {
                strWhere += String.format(" and pfbz='%s'", params.get("pfbz").toString());
            }
            if (params.containsKey("organ") && (org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("organ").toString()))) {
                String organ = params.get("organ").toString();
                if(organ.equals("1306020010")){
                    strWhere += " and (qybh='1306020010' or qybh='1306020001' or qybh='1733227357')";
                }
                else {
                    String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                    if (org.apache.commons.lang3.StringUtils.isNoneBlank(strOrgans)) {
                        strOrgans = organ + "," + strOrgans;
                        strOrgans = strOrgans.replace(",", "','");
                        strWhere += String.format(" and qybh in ('%s')", strOrgans);
                    } else {
                        strWhere += String.format(" and qybh='%s'", organ);
                    }
                }
            }

            if (params.containsKey("cphm") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("cphm").toString())) {
                strWhere += " and cphm like '%" + params.get("cphm").toString() + "%'";
            }
            if (params.containsKey("cpys") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("cpys").toString())) {
                String spzl = params.get("cpys").toString();
                strWhere += String.format(" and cpys='%s'", spzl);
            }
            if (params.containsKey("kssj") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and jcsj  > '%s 00:00:00'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and jcsj  <= '%s 23:59:59'", params.get("jssj").toString());
            }
            SimpleDateFormat sdfsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            logger.info("任务[" + taskId + "]查询条件：" + strWhere);

            // 4. 查询总条数（用于计算进度）
            String countSql="select count(*) from mj_data_dztz WITH(NOLOCK)  where "+strWhere;
            int total = publicService.selectCount(countSql);
            if (total <= 0) {
                ExportProgressMonitor.getInstance().updateProgress(taskId, 100);
                ExportProgressMonitor.getInstance().setFilepath(taskId, "");
                logger.info("任务[" + taskId + "]无数据，导出完成");
                return null;
            }
            ExportProgressMonitor.getInstance().updateProgress(taskId, 10);
            String fileName = "txjl_" + taskId + ".xls";
            String filePath = exportDir + File.separator + fileName;
            File exportDirFile = new File(exportDir);
            if (!exportDirFile.exists() && !exportDirFile.mkdirs()) {
                throw new RuntimeException("创建导出目录失败：" + exportDir);
            }
            util = new ExcelExportUtilex(ExcelExportUtilex.ExcelType.XLS, filePath);

            // 4. 构建并写入表头（不变）
            List<ExcelCell> headerList = new ArrayList<>();
            int col = 0;
            headerList.add(new ExcelCell(0, col++, "进厂出入口编号"));
            headerList.add(new ExcelCell(0, col++, "进厂道闸编号"));
            headerList.add(new ExcelCell(0, col++, "出厂出入口编号"));
            headerList.add(new ExcelCell(0, col++, "出厂道闸编号"));
            headerList.add(new ExcelCell(0, col++, "进厂时间"));
            headerList.add(new ExcelCell(0, col++, "出厂时间"));
            headerList.add(new ExcelCell(0, col++, "车牌号码"));
            headerList.add(new ExcelCell(0, col++, "号牌颜色"));
            headerList.add(new ExcelCell(0, col++, "车辆类型"));
            headerList.add(new ExcelCell(0, col++, "注册登记日期"));
            headerList.add(new ExcelCell(0, col++, "车辆识别代号(VIN)"));
            headerList.add(new ExcelCell(0, col++, "发动机号"));
            headerList.add(new ExcelCell(0, col++, "车辆品牌型号"));
            headerList.add(new ExcelCell(0, col++, "燃料类型"));
            headerList.add(new ExcelCell(0, col++, "排放标准"));
            headerList.add(new ExcelCell(0, col++, "联网状态"));
            headerList.add(new ExcelCell(0, col++, "使用性质"));
            headerList.add(new ExcelCell(0, col++, "进厂运输货物名称"));
            headerList.add(new ExcelCell(0, col++, "进厂运输量"));
            headerList.add(new ExcelCell(0, col++, "出厂运输货物名称"));
            headerList.add(new ExcelCell(0, col++, "出厂运输量"));
            headerList.add(new ExcelCell(0, col++, "车队名称"));
            headerList.add(new ExcelCell(0, col++, "进厂照片"));
            headerList.add(new ExcelCell(0, col++, "出厂照片"));
            headerList.add(new ExcelCell(0, col++, "随车清单"));
            headerList.add(new ExcelCell(0, col++, "行驶证"));
            util.writeHeader(headerList);
            headerList.clear();

            // 5. 分页查询并写入数据（核心修复部分）
            int pageSize = 1000;
            int totalPages = (total + pageSize - 1) / pageSize;
            logger.info("任务[" + taskId + "]总页数：" + totalPages);
            int currentPage = 1;

            while (currentPage <= totalPages) {
                int start = (currentPage - 1) * pageSize + 1;
                int end = currentPage * pageSize;
                // 子查询分页：先排序，再通过 ROW_NUMBER() 编号，最后取编号在 [start, end] 之间的数据
                String dataSql = "select jccrkbh,jcdzbh,cccrkbh,ccdzbh,jcsj,ccsj,c.cphm,c.cpys,b.oi_value as cllx,c.ccdjrq,c.clsbdh,c.clppxh,c.rlzl,c.pfbz,c.lwzt,c.syxz,"+
                        "jcyshwmc,jcysl,ccyshwmc,ccysl,c.syr as cdmc,e.syr,e.fdjh,cczp,jczp,cczp,c.scqdzp,xszzp from (SELECT * FROM (  SELECT *, ROW_NUMBER() OVER (ORDER BY id) AS RowNum  FROM mj_data_dztz WITH(NOLOCK)" +
                        "   WHERE "+strWhere+") AS temp WHERE RowNum BETWEEN " + start + " AND " + end+")c " +
                        "left join ai_sys_code b on c.cllx=b.oi_code and b.oi_name='车辆类型' left join mj_vehicle as e on c.cphm=e.cphm and c.cpys=e.cpys order by jcsj";
                logger.info(dataSql);

                List<MjDataDztzX> mjDataDztzXs = publicService.selectObjs(dataSql, MjDataDztzX.class);
                logger.info("第" + currentPage + "页查询完成，数据量：" + mjDataDztzXs.size());

                List<ExcelCell> cellList = new ArrayList<>(pageSize * 15);
                int localRow = 0; // 局部行号从0开始，连续递增
                ConverUtil converUtil=new ConverUtil();
                for (MjDataDztzX mjDataDztz : mjDataDztzXs) {
                    col = 0;
                    // 【修复2】局部行号用localRow，且循环内只递增1次（在循环结尾）
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getJccrkbh()));
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getJcdzbh()));
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getCccrkbh()));
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getCcdzbh()));
                    cellList.add(new ExcelCell(localRow, col++, sdfsj.format(mjDataDztz.getJcsj())));
                    if(mjDataDztz.getCcsj()!=null) {
                        cellList.add(new ExcelCell(localRow, col++, sdfsj.format(mjDataDztz.getCcsj())));
                    }
                    else{
                        cellList.add(new ExcelCell(localRow, col++, ""));
                    }
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getCphm()));
                    cellList.add(new ExcelCell(localRow, col++, ConvertCpys(mjDataDztz.getCpys())));
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getCllx()));
                    if(mjDataDztz.getCcdjrq()!=null) {
                        cellList.add(new ExcelCell(localRow, col++, sdf.format(mjDataDztz.getCcdjrq())));
                    }
                    else{
                        cellList.add(new ExcelCell(localRow, col++, ""));
                    }
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getClsbdh()));
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getFdjh()));
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getClppxh()));
                    cellList.add(new ExcelCell(localRow, col++, converUtil.convertrlzlex(mjDataDztz.getRlzl())));
                    cellList.add(new ExcelCell(localRow, col++, converUtil.pfsztopfhz(mjDataDztz.getPfbz())));
                    cellList.add(new ExcelCell(localRow, col++, converUtil.convertlwzt(mjDataDztz.getLwzt())));
                    cellList.add(new ExcelCell(localRow, col++, ConvertSyxz(mjDataDztz.getSyxz())));
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getJcyshwmc()));
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getJcysl()));
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getCcyshwmc()));
                    cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getCcysl()));
                    if(mjDataDztz.getCdmc() != null && !mjDataDztz.getCdmc().isEmpty()){
                        cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getCdmc()));
                    }
                    else {
                        cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getSyr()));
                    }
                    if(mjDataDztz.getJczp()!=null&&!mjDataDztz.getJczp().equals("")){
                        cellList.add(new ExcelCell(localRow, col++, photourl+mjDataDztz.getJczp()));
                    }
                    else{
                        cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getJczp()));
                    }
                    if(mjDataDztz.getCczp()!=null&&!mjDataDztz.getCczp().equals("")){
                        cellList.add(new ExcelCell(localRow, col++, photourl+mjDataDztz.getCczp()));
                    }
                    else{
                        cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getCczp()));
                    }
                    if(mjDataDztz.getScqdzp()!=null&&!mjDataDztz.getScqdzp().equals("")){
                        cellList.add(new ExcelCell(localRow, col++,photourl+mjDataDztz.getScqdzp()));
                    }
                    else{
                        cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getScqdzp()));
                    }
                    if(mjDataDztz.getXszzp()!=null&&!mjDataDztz.getXszzp().equals("")){
                        cellList.add(new ExcelCell(localRow, col++,photourl+mjDataDztz.getXszzp()));
                    }
                    else{
                        cellList.add(new ExcelCell(localRow, col++, mjDataDztz.getXszzp()));
                    }
                    localRow++;
                }

                // 分页写入Excel（不变）
                util.writePage(cellList); // 调用修复后的writePage方法
                cellList.clear();

                // 优化进度计算（可选：避免进度提前到100%）
                int progress = 10 + (int) (currentPage * 1.0 / totalPages * 90); // 10%基础进度 + 90%数据进度
                progress = Math.min(progress, 99); // 留1%给finish后更新100%
                ExportProgressMonitor.getInstance().updateProgress(taskId, progress);
                logger.info("任务[" + taskId + "]进度更新为：" + progress + "%");

                currentPage++;
            }

            // 6. 完成导出（不变）
            util.finish();
            logger.info("任务[" + taskId + "]Excel文件生成成功，路径：" + filePath);
            ExportProgressMonitor.getInstance().updateProgress(taskId, 100);
            ExportProgressMonitor.getInstance().setFilepath(taskId, filePath);
            logger.info("任务[" + taskId + "]导出完成");

        } catch (Exception e) {
            logger.error("任务[" + taskId + "]导出失败：" + e.getMessage(), e);
            ExportProgressMonitor.getInstance().updateProgress(taskId, -2);
            // 【新增】异常时关闭资源，避免文件占用
            if (util != null) {
                try {
                    util.finish();
                } catch (IOException ex) {
                    logger.error("关闭Excel工具失败", ex);
                }
            }
        }
        return null;
    }
    // 辅助方法：格式化日期（假设tgkssj是Date类型或字符串）
    private String formatDate(Object dateObj) {
        if (dateObj == null) {
            return "";
        }
        // 若dateObj是Date类型
        if (dateObj instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format((Date) dateObj);
        }
        // 若dateObj是字符串（直接返回或二次格式化）
        return dateObj.toString();
    }
    // 辅助方法：构建查询条件（避免SQL注入，参数需做非空判断）
    private String buildQueryCondition(Map<String, Object> params) {
        StringBuilder strWhere = new StringBuilder("1=1");

        // 从params中获取前端传入的参数（与startExport接口的params对应）
        String cphm = (String) params.get("cphm"); // 车牌号码
        String cpys = (String) params.get("cpys"); // 车牌颜色
        String organ = (String) params.get("organ"); // 企业编号
        String kssj = (String) params.get("kssj"); // 开始时间
        String jssj = (String) params.get("jssj"); // 结束时间
        // 可根据实际需求添加更多参数

        // 拼接条件（非空才添加，避免多余的条件）
        if (StringUtils.hasText(organ)) {
            strWhere.append(" and qybh = '").append(escapeSql(organ)).append("'");
        }
        if (StringUtils.hasText(cphm)) {
            strWhere.append(" and licensePlate = '").append(escapeSql(cphm)).append("'");
        }
        if (StringUtils.hasText(cpys)) {
            strWhere.append(" and licensePlateColor = '").append(escapeSql(cpys)).append("'");
        }
        if (StringUtils.hasText(kssj)) {
            strWhere.append(" and tgkssj >= '").append(escapeSql(kssj)).append("'");
        }
        if (StringUtils.hasText(jssj)) {
            strWhere.append(" and tgkssj <= '").append(escapeSql(jssj)).append("'");
        }

        return strWhere.toString();
    }
    private String escapeSql(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("'", "''"); // 将单引号替换为两个单引号（SQL标准转义）
    }
}

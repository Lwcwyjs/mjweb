package com.task;

import com.model.local.MjBmd;
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

/**
 * 异步导出任务
 */
public class ExcelExportBmdTask implements Callable<Void> {
    private final String taskId;
    private final String exportDir; // Excel临时存储目录（如：/opt/temp-excel/）

    // 注入你需要的Service（根据实际项目修改，可通过构造函数传入）
    private final SysOptionService sysOptionService;
    private final PublicService publicService;
    private final SysOrganizationService sysOrganizationService;
    private final Logger logger;

    public ExcelExportBmdTask(String taskId, String exportDir,
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
            String strWhere = " 1=1 ";
            if (params.containsKey("organ") && (org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("organ").toString()))) {
                String organ = params.get("organ").toString();
                if (organ.equals("1306020010")) {
                    strWhere += " and (organ='1306020010' or organ='1306020001' or organ='1733227357')";
                } else {
                    String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                    if (org.apache.commons.lang3.StringUtils.isNoneBlank(strOrgans)) {
                        strOrgans = organ + "," + strOrgans;
                        strOrgans = strOrgans.replace(",", "','");
                        strWhere += String.format(" and organ in ('%s')", strOrgans);
                    } else {
                        strWhere += String.format(" and organ='%s'", organ);
                    }
                }
            }

            if (params.containsKey("cphm") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("cphm").toString())) {
                strWhere += " and cphm like '%" + params.get("cphm").toString() + "%'";
            }
            if (params.containsKey("cpys") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("cpys").toString())) {
                String cpys = params.get("cpys").toString();
                strWhere += String.format(" and cpys='%s'", cpys);
            }
            SimpleDateFormat sdfsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            logger.info("任务[" + taskId + "]查询条件：" + strWhere);

            // 4. 查询总条数（用于计算进度）
            String countSql = "select count(*) from mj_bmd  where " + strWhere;
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
            headerList.add(new ExcelCell(0, col++, "车牌号码"));
            headerList.add(new ExcelCell(0, col++, "号牌颜色"));
            headerList.add(new ExcelCell(0, col++, "姓名"));
            headerList.add(new ExcelCell(0, col++, "联系电话"));
            headerList.add(new ExcelCell(0, col++, "开始时间"));
            headerList.add(new ExcelCell(0, col++, "结束时间"));
            headerList.add(new ExcelCell(0, col++, "可通行门"));
            headerList.add(new ExcelCell(0, col++, "备注"));
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
                String dataSql = "select * from (SELECT cphm,case when cpys='1' then '黄色' when cpys='0' then '蓝色' when cpys='4' then '绿色' when cpys='6' " +
                        "then '黄绿色' else '' end as cpys,czmc,lxdh,sxsj,zzsj,crkbh,bz, ROW_NUMBER() OVER (ORDER BY id) AS RowNum   FROM mj_bmd " +
                        "   WHERE  1=1  and  cjsj  <= '2025-11-09 23:59:59') AS " +
                        "temp WHERE RowNum BETWEEN " + start + " AND " + end;
                logger.info(dataSql);

                List<MjBmd> mjBmds = publicService.selectObjs(dataSql, MjBmd.class);
                logger.info("第" + currentPage + "页查询完成，数据量：" + mjBmds.size());

                List<ExcelCell> cellList = new ArrayList<>(pageSize * 15);
                int localRow = 0; // 局部行号从0开始，连续递增
                ConverUtil converUtil = new ConverUtil();
                for (MjBmd mjBmd : mjBmds) {
                    col = 0;
                    // 【修复2】局部行号用localRow，且循环内只递增1次（在循环结尾）
                    cellList.add(new ExcelCell(localRow, col++, mjBmd.getCphm()));
                    cellList.add(new ExcelCell(localRow, col++, mjBmd.getCpys()));
                    cellList.add(new ExcelCell(localRow, col++, mjBmd.getCzmc()));
                    cellList.add(new ExcelCell(localRow, col++, mjBmd.getLxdh()));
                    cellList.add(new ExcelCell(localRow, col++, sdf.format(mjBmd.getSxsj())));
                    cellList.add(new ExcelCell(localRow, col++, sdf.format(mjBmd.getZzsj())));
                    cellList.add(new ExcelCell(localRow, col++, mjBmd.getCrkbh()));
                    cellList.add(new ExcelCell(localRow, col++, mjBmd.getBz()));
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

package com.task;

import com.model.vehmanage.MjFdlVehicle;
import com.service.base.PublicService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
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
public class ExcelExportFdlTask implements Callable<Void> {
    private final String taskId;
    private final String exportDir; // Excel临时存储目录（如：/opt/temp-excel/）

    // 注入你需要的Service（根据实际项目修改，可通过构造函数传入）
    private final SysOptionService sysOptionService;
    private final PublicService publicService;
    private final SysOrganizationService sysOrganizationService;
    private final Logger logger;

    public ExcelExportFdlTask(String taskId, String exportDir,
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
            if (params.containsKey("organ") && (org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("organ").toString()))) {
                String organ = params.get("organ").toString();
                String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                if (org.apache.commons.lang3.StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = organ + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and qybh in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and qybh='%s'", organ);
                }
            }
            if (params.containsKey("pfbz") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("pfbz").toString())) {
                strWhere += String.format(" and pfbz='%s'", params.get("pfbz").toString());
            }
            if (params.containsKey("hbdjhm") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("hbdjhm").toString())) {
                strWhere += " and hbdjhm like '%" + params.get("hbdjhm").toString() + "%'";
            }
            if (params.containsKey("kssj") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and cjsj  > '%s 00:00:00'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and cjsj  <= '%s 23:59:59'", params.get("jssj").toString());
            }
            SimpleDateFormat sdfsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            logger.info("任务[" + taskId + "]查询条件：" + strWhere);

            // 4. 查询总条数（用于计算进度）
            String countSql="select count(*) "+
                    "from (select qybh from mj_fdl_vehicle  where "+strWhere+") a left join ai_sys_organization b on a.qybh=b.organ";
            int total = publicService.selectCount(countSql);
            if (total <= 0) {
                ExportProgressMonitor.getInstance().updateProgress(taskId, 100);
                ExportProgressMonitor.getInstance().setFilepath(taskId, "");
                logger.info("任务[" + taskId + "]无数据，导出完成");
                return null;
            }
            ExportProgressMonitor.getInstance().updateProgress(taskId, 10);
            String fileName = "fdlvehicle_" + taskId + ".xls";
            String filePath = exportDir + File.separator + fileName;
            File exportDirFile = new File(exportDir);
            if (!exportDirFile.exists() && !exportDirFile.mkdirs()) {
                throw new RuntimeException("创建导出目录失败：" + exportDir);
            }
            util = new ExcelExportUtilex(ExcelExportUtilex.ExcelType.XLS, filePath);

            // 4. 构建并写入表头（不变）
            List<ExcelCell> headerList = new ArrayList<>();
            int col = 0;
            headerList.add(new ExcelCell(0, col++, "企业名称"));
            headerList.add(new ExcelCell(0, col++, "车牌号码"));
            headerList.add(new ExcelCell(0, col++, "环保登记号码"));
            headerList.add(new ExcelCell(0, col++, "产品识别码"));
            headerList.add(new ExcelCell(0, col++, "机械环保代码"));
            headerList.add(new ExcelCell(0, col++, "机械型号"));
            headerList.add(new ExcelCell(0, col++, "生产日期"));
            headerList.add(new ExcelCell(0, col++, "机械种类"));
            headerList.add(new ExcelCell(0, col++, "发动机编号"));
            headerList.add(new ExcelCell(0, col++, "发动机生产厂"));
            headerList.add(new ExcelCell(0, col++, "发动机型号"));
            headerList.add(new ExcelCell(0, col++, "排放标准"));
            headerList.add(new ExcelCell(0, col++, "燃料种类"));
            headerList.add(new ExcelCell(0, col++, "所有人"));
            headerList.add(new ExcelCell(0, col++, "发动机铭牌照片"));
            headerList.add(new ExcelCell(0, col++, "整车铭牌照片"));
            headerList.add(new ExcelCell(0, col++, "环保标签照片"));
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
                String dataSql = "select b.name as qybh,cphm,hbdjhm,cpsbm,jxhbdm,jxxh,jxzl,scrq,fdjbh,fdjscc,fdjxh,fdjmpzp,zcmpzp,hbbqzp,pfbz,rlzl,syr " +
                        "from (select qybh,cphm,hbdjhm,cpsbm,jxhbdm,jxxh,scrq,case when jxzl='1' then '装载机' when jxzl='2' then '挖掘机'"+
                        " when jxzl='3' then '推土机' when jxzl='4' then '叉车' when jxzl='5' then '非公路用卡车' when jxzl='6' then '其他'" +
                        " else '' end as jxzl,fdjbh,fdjscc,fdjxh,fdjmpzp,zcmpzp,hbbqzp," +
                        "case when pfbz='1' then '国Ⅰ' when pfbz='2' then '国Ⅱ' when pfbz='3' then '国Ⅲ' when pfbz='4' then '国Ⅳ' "  +
                        "when pfbz='5' then '国Ⅴ' when pfbz='6' then '国Ⅵ' when pfbz='D' then '电动' else '' end as pfbz,"  +
                        "case when rlzl='A' then '汽油' when rlzl='B' then '柴油' when rlzl='C' then '电' when rlzl='E' then "+
                        "'天然气' when rlzl='Z' then '其他' else rlzl end as rlzl,syr from (SELECT * FROM ("
                        + "  SELECT *, ROW_NUMBER() OVER (ORDER BY id) AS RowNum "  // 必须排序
                        + "  FROM mj_fdl_vehicle "
                        + "  WHERE " + strWhere
                        + ") AS temp "
                        + "WHERE RowNum BETWEEN " + start + " AND " + end+")c)d left join ai_sys_organization b on d.qybh=b.organ";
                logger.info(dataSql);

                List<MjFdlVehicle> mjFdlVehicles = publicService.selectObjs(dataSql, MjFdlVehicle.class);
                logger.info("第" + currentPage + "页查询完成，数据量：" + mjFdlVehicles.size());

                List<ExcelCell> cellList = new ArrayList<>(pageSize * 15);
                int localRow = 0; // 局部行号从0开始，连续递增
                for (MjFdlVehicle mjFdlVehicle : mjFdlVehicles) {
                    col = 0;
                    // 【修复2】局部行号用localRow，且循环内只递增1次（在循环结尾）
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getQybh()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getCphm()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getHbdjhm()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getCpsbm()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getJxhbdm()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getJxxh()));
                    cellList.add(new ExcelCell(localRow, col++, sdfsj.format(mjFdlVehicle.getScrq())));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getJxzl()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getFdjbh()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getFdjscc()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getFdjxh()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getPfbz()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getRlzl()));
                    cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getSyr()));
                    if (mjFdlVehicle.getFdjmpzp() != null && !mjFdlVehicle.getFdjmpzp().equals("")) {
                        cellList.add(new ExcelCell(localRow, col++, photourl + mjFdlVehicle.getFdjmpzp()));
                    } else {
                        cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getFdjmpzp()));
                    }
                    if (mjFdlVehicle.getZcmpzp() != null && !mjFdlVehicle.getZcmpzp().equals("")) {
                        cellList.add(new ExcelCell(localRow, col++, photourl + mjFdlVehicle.getZcmpzp()));
                    } else {
                        cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getZcmpzp()));
                    }
                    if (mjFdlVehicle.getHbbqzp() != null && !mjFdlVehicle.getHbbqzp().equals("")) {
                        cellList.add(new ExcelCell(localRow, col++, photourl + mjFdlVehicle.getHbbqzp()));
                    } else {
                        cellList.add(new ExcelCell(localRow, col++, mjFdlVehicle.getHbbqzp()));
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

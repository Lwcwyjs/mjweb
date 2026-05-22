package com.task;

import com.commons.annotation.support.ValidateService;
import com.model.local.MjBmd;
import com.service.base.PublicService;
import com.service.sysmanage.SysOrganizationService;
import com.util.ConverUtil;
import com.util.ExcelImportUtilex;
import com.util.ImportProgressMonitor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * 异步导入任务（对应导出任务，保持任务ID机制一致）
 */
public class ExcelImportBmdTask implements Callable<Void> {
    private final String taskId;
    private final String importFilePath; // 上传的Excel文件路径
    private final PublicService publicService;
    private final SysOrganizationService sysOrganizationService;
    private final Logger logger;

    // 构造函数传入依赖（与导出任务一致的注入方式）
    public ExcelImportBmdTask(String taskId, String importFilePath,
                              PublicService publicService,
                              SysOrganizationService sysOrganizationService,
                              Logger logger) {
        this.taskId = taskId;
        this.importFilePath = importFilePath;
        this.publicService = publicService;
        this.sysOrganizationService = sysOrganizationService;
        this.logger = logger;
    }

    @Override
    public Void call() throws Exception {
        ExcelImportUtilex importUtil = null;
        try {
            // 1. 初始化进度（0%）
            ImportProgressMonitor.getInstance().updateProgress(taskId, 0);
            logger.info("导入任务[" + taskId + "]开始执行");

            // 2. 读取Excel文件（占20%进度）
            importUtil = new ExcelImportUtilex(importFilePath);
            int totalRows = importUtil.getTotalDataRows(); // 获取数据总行数（排除表头）
            if (totalRows <= 0) {
                logger.info("导入任务[" + taskId + "]无有效数据");
                ImportProgressMonitor.getInstance().updateProgress(taskId, 100);
                return null;
            }
            ImportProgressMonitor.getInstance().updateProgress(taskId, 20); // 文件读取完成
            ConverUtil converUtil=new ConverUtil();
            // 3. 分批次解析数据（每批1000条，占30%进度）
            int batchSize = 1000;
            int totalBatches = (totalRows + batchSize - 1) / batchSize;
            int currentBatch = 1;
            while (currentBatch <= totalBatches) {
                List<MjBmd> dataList = importUtil.readBatchData(currentBatch, batchSize, MjBmd.class);
                for (MjBmd mjBmd : dataList) {
                    List<String> bindResult = new ArrayList<String>();
                    mjBmd.setCpys(converUtil.ConvertCpys(mjBmd.getCpys()));
                    mjBmd.setCjsj(new Date());
                    UUID uuid = UUID.randomUUID();
                    String uuids = uuid.toString().replaceAll("-", "");
                    mjBmd.setId(uuids);
                    mjBmd.setOrgan("5869040498");
                    bindResult = ValidateService.valid(mjBmd);

                    if (bindResult.size() == 0) {
                        // 验证用户是否已存在
                        MjBmd mj = publicService.selectObj("mj_bmd","","cphm,cpys",mjBmd);
                        if (mj != null) {
                            publicService.update("mj_bmd","id","cphm,cpys",mjBmd);
                        }
                        else {
                            publicService.insert("mj_bmd","","",mjBmd);
                        }
                    }
                    else{
                        logger.info(mjBmd.getCphm()+"导入失败:"+bindResult.toString());
                    }
                }
                // 解析进度更新（20% + 30% * 批次占比）
                int progress = 10 + (int) (currentBatch * 1.0 / totalBatches * 90); // 10%基础进度 + 90%数据进度
                progress = Math.min(progress, 99); // 留1%给finish后更新100%
                ImportProgressMonitor.getInstance().updateProgress(taskId, progress);
                logger.info("导入任务[" + taskId + "]第" + currentBatch + "批解析完成，数据量：" + dataList.size());
                currentBatch++;
            }
            // 6. 导入完成（100%）
            ImportProgressMonitor.getInstance().updateProgress(taskId, 100);
            ImportProgressMonitor.getInstance().setMsg(taskId, "导入成功，共" + totalRows + "条数据");
            logger.info("导入任务[" + taskId + "]执行完成");

        } catch (Exception e) {
            logger.error("导入任务[" + taskId + "]失败：" + e.getMessage(), e);
            ImportProgressMonitor.getInstance().updateProgress(taskId, -2); // 失败标识
            ImportProgressMonitor.getInstance().setErrorMsg(taskId, e.getMessage());
        } finally {
            // 关闭工具类，删除临时文件（可选）
            if (importUtil != null) {
                importUtil.close();
            }
            File tempFile = new File(importFilePath);
            if (tempFile.exists()) {
                FileUtils.deleteQuietly(tempFile);
            }
        }
        return null;
    }
}
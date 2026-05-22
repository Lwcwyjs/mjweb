package com.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 导出进度监控器（单例模式）
 */
public class ExportProgressMonitor {
    // 单例实例
    private static final ExportProgressMonitor INSTANCE = new ExportProgressMonitor();
    // 任务ID -> 进度(0-100)：-1=未开始，-2=失败，100=完成
    private final Map<String, Integer> progressMap = new HashMap<>();
    // 任务ID -> 生成的Excel文件路径
    private final Map<String, String> fileMap = new HashMap<>();
    // 任务ID -> 导出参数（用于异步处理）
    private final Map<String, Map<String, Object>> paramMap = new HashMap<>();

    private ExportProgressMonitor() {}

    public static ExportProgressMonitor getInstance() {
        return INSTANCE;
    }

    // 生成唯一任务ID
    public String generateTaskId() {
        String taskId = UUID.randomUUID().toString().replace("-", "");
        progressMap.put(taskId, 0); // 初始进度0%
        return taskId;
    }

    // 存储导出参数
    public void setParams(String taskId, Map<String, Object> params) {
        paramMap.put(taskId, params);
    }

    // 获取导出参数
    public Map<String, Object> getParams(String taskId) {
        return paramMap.get(taskId);
    }

    // 更新进度
    public void updateProgress(String taskId, int progress) {
        progressMap.put(taskId, Math.min(100, progress));
    }

    // 获取进度
    public int getProgress(String taskId) {
        return progressMap.getOrDefault(taskId, -1);
    }

    // 存储文件路径（导出完成后）
    public void setFilepath(String taskId, String filepath) {
        fileMap.put(taskId, filepath);
    }

    // 获取文件路径
    public String getFilepath(String taskId) {
        return fileMap.get(taskId);
    }

    // 清理任务（避免内存泄漏）
    public void clearTask(String taskId) {
        progressMap.remove(taskId);
        fileMap.remove(taskId);
        paramMap.remove(taskId);
    }
}
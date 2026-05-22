package com.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 导出/导入进度监控器（单例模式，用于跨线程共享任务进度、参数、结果）
 */
public class ImportProgressMonitor {
    // 单例实例
    private static volatile ImportProgressMonitor instance;
    // 任务进度存储（taskId -> 进度值）
    private final Map<String, Integer> progressMap = new ConcurrentHashMap<>();
    // 任务参数存储（taskId -> 参数Map）
    private final Map<String, Map<String, Object>> paramsMap = new ConcurrentHashMap<>();
    // 任务成功消息存储（taskId -> 成功提示）
    private final Map<String, String> msgMap = new ConcurrentHashMap<>();
    // 任务错误消息存储（taskId -> 错误提示）
    private final Map<String, String> errorMsgMap = new ConcurrentHashMap<>();
    // 任务文件路径存储（导出场景用，导入可扩展）
    private final Map<String, String> filePathMap = new ConcurrentHashMap<>();

    // 私有构造函数，防止外部实例化
    private ImportProgressMonitor() {}

    // 单例获取方法
    public static ImportProgressMonitor getInstance() {
        if (instance == null) {
            synchronized (ImportProgressMonitor.class) {
                if (instance == null) {
                    instance = new ImportProgressMonitor();
                }
            }
        }
        return instance;
    }

    // ********** 进度相关方法 **********
    public void updateProgress(String taskId, int progress) {
        progressMap.put(taskId, progress);
    }

    public int getProgress(String taskId) {
        return progressMap.getOrDefault(taskId, -1); // -1表示任务不存在
    }

    // ********** 参数相关方法 **********
    public void setParams(String taskId, Map<String, Object> params) {
        paramsMap.put(taskId, params);
    }

    public Map<String, Object> getParams(String taskId) {
        return paramsMap.getOrDefault(taskId, new HashMap<>());
    }

    // ********** 消息相关方法 **********
    public void setMsg(String taskId, String msg) {
        msgMap.put(taskId, msg);
    }

    public String getMsg(String taskId) {
        return msgMap.getOrDefault(taskId, "");
    }

    public void setErrorMsg(String taskId, String errorMsg) {
        errorMsgMap.put(taskId, errorMsg);
    }

    public String getErrorMsg(String taskId) {
        return errorMsgMap.getOrDefault(taskId, "");
    }

    // ********** 文件路径相关方法（导出场景用） **********
    public void setFilepath(String taskId, String filePath) {
        filePathMap.put(taskId, filePath);
    }

    public String getFilepath(String taskId) {
        return filePathMap.getOrDefault(taskId, "");
    }

    // 可选：任务完成后清理（防止内存泄漏）
    public void clearTask(String taskId) {
        progressMap.remove(taskId);
        paramsMap.remove(taskId);
        msgMap.remove(taskId);
        errorMsgMap.remove(taskId);
        filePathMap.remove(taskId);
    }
}
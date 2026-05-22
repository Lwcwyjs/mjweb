package com.util;

import java.lang.annotation.*;

/**
 * Excel列名映射注解（用于指定实体类字段对应Excel表头）
 */
@Target({ElementType.FIELD}) // 仅作用于字段
@Retention(RetentionPolicy.RUNTIME) // 运行时可获取
@Documented
public @interface ExcelColumn {
    String name(); // 对应Excel表头名称（如"车牌号码"）
    boolean required() default false; // 是否为必填字段（默认非必填）
}
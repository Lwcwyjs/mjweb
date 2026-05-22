package com.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @author songdh
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
public @interface DimensioningZoneAnalysis {
	//参数或者字段描述,这样能够显示友好的异常信息
	String description() default "";
	//描述划区标注角list，1-一维集合，2-二位集合
	int zoneType() default 1;
}

package com.commons.annotation;

import java.lang.annotation.*;

/**
 * 用户操作行为日志
 * Created by songdh on 2018/9/20.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface activeLog {
	/**
	 * operateCondition:操作简述
	 * @return
	 */
     String operateCondition() default "";
     /**
 	 * operateModule:模块名
 	 * @return
 	 */
      String operateModule() default "其他";
     /**
 	 * operateTypel:操作类型--0:登录日志
	 * 1:查询日志
	 * 2:新增日志
	 * 3修改日志
	 * 4删除日志
	 *
 	 * @return
 	 */
     String operateType() default "";
     /**
 	 * operateResult:操作结果
 	 * 1:成功
	 * 0:失败
 	 * @return
 	 */
     String operateResult() default "";
}

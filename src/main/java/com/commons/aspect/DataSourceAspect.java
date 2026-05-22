package com.commons.aspect;

import com.commons.annotation.DataSourceChange;
import com.commons.datasource.DynamicDataSource;
import com.commons.exception.DataSourceAspectException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 有{@link DataSourceChange}注解的方法，调用时会切换到指定的数据源
 *
 * @author tanghd
 */
@Aspect
public class DataSourceAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAspect.class);

    @Pointcut(value = "@annotation(com.commons.annotation.DataSourceChange)")
    private void changeDS() {}

    @Around(value = "changeDS()", argNames = "pjp")
    public Object doAround(ProceedingJoinPoint pjp) {
        Object retVal = null;
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        DataSourceChange annotation = method.getAnnotation(DataSourceChange.class);
        boolean selectedDataSource = false;
        try {
            if (null != annotation) {
                selectedDataSource = true;
                if (annotation.slave()) {
                    DynamicDataSource.useSlave();
                } else {
                    DynamicDataSource.useMaster();
                }
            }
            retVal = pjp.proceed();
        } catch (Throwable e) {
            LOGGER.warn("数据源切换错误", e);
            throw new DataSourceAspectException("数据源切换错误", e);
        } finally {
            if (selectedDataSource) {
                DynamicDataSource.reset();
            }
        }
        return retVal;
    }
}

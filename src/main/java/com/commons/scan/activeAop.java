package com.commons.scan;

import com.commons.scan.aspectService.aspectServiceImp;
import com.commons.scan.aspectService.inter.aspectService;
import com.service.base.PublicService;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by song on 2018/9/21.
 */
@Aspect
@Component
public class activeAop {
	@Autowired
	@Resource
	private PublicService publicService;
    /**
     * 切入controll public 方法 规则自定义
     */
    @Pointcut("execution(public * com.song.controller.*.*(..))")
    public void writControll(){};
    /**
     *切入注解
     */
    @Pointcut("@annotation(com.commons.annotation.activeLog)")
    public void writAnnotation() {
    }


//    @AfterReturning(returning = "ret", pointcut = "writAnnotation()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        System.out.println("方法的返回值 : " + ret);

    }

    //后置异常通知
    @AfterThrowing("writAnnotation()")
    public void throwss(JoinPoint jp){
        System.out.println("方法异常时执行.....");
        System.out.printf("方法的返回值 : "+jp.getTarget().toString());
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("writAnnotation()")
    public void after(JoinPoint jp){
        System.out.println("方法最后执行.....");
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("writAnnotation()")
    public Object arround(ProceedingJoinPoint pjp) {
        System.out.println("方法环绕start.....");
        try {
            Object o =  pjp.proceed();
            aspectService ass = new aspectServiceImp();
            ass.aspectLogInfo(pjp,publicService);
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return e;
        }
    }



}

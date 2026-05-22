package com.commons.scan.aspectService;

import com.commons.annotation.activeLog;
import com.commons.scan.aspectService.inter.aspectService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import com.service.base.PublicService;
public class aspectServiceImp implements aspectService {

	@Override
	public void aspectLogInfo(ProceedingJoinPoint pjp,PublicService publicService) {
		// TODO Auto-generated method stub

		Signature sig = pjp.getSignature();
		if (!(sig instanceof MethodSignature)) {
			throw new IllegalArgumentException("该注解只能用于方法");
		}
		
		String classname = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();
		Object o = new Object();
		try {
			Object[] args = pjp.getArgs(); // 获取被切函数 参数
			//o = pjp.proceed();  //此步骤会导致 controller 重新执行一边，建议去掉
			
			MethodSignature msig = null;
		    activeLog al = ((MethodSignature)pjp.getSignature()).getMethod().getAnnotation(activeLog.class);

		    String operateCondition = "";

		    operateCondition = al.operateCondition()+"==[类名]:"
//		    		+classname+"[方法]:"+methodName+"[参数]:"+JSON.toJSONString(args);
		    		+classname+"[方法]:"+methodName+"[参数]:(";
		    
		    for(Object ob : args){
		    	if (ob!=null) {
					operateCondition = operateCondition + ob.toString() + ",";
				}
		    }

		    operateCondition = operateCondition.substring(0, operateCondition.length()-1)+")";
		    if(operateCondition.length()>3990){
		    	operateCondition = operateCondition.substring(0, operateCondition.length()-1);
		    }
		    String operateResult = "";
		    operateResult = al.operateResult();
		    String operateType = "";
		    operateType = al.operateType();
		    String operatemodule = "";
		    operatemodule = al.operateModule();


		
		}/* catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} */catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

		
	}

}

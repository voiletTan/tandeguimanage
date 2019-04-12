package com.tandegui.operationlogaop;

import com.tandegui.annotation.SystemLog;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户操作拦截AOP
 * 
 * @project datacenter
 * @class com.tandegui.operationlogaop.LogAopAction
 * @copyright www.tandegui.com
 * @author Waiter Tan
 * @date 2019年4月11日
 * @description
 */
@Aspect
@Component
public  class LogAop {
	final static Logger logger = Logger.getLogger(LogAop.class);
	
	//@Autowired
	//private UserOperationLogService userOperationLogService;
	
    @Pointcut("@annotation(com.tandegui.annotation.SystemLog)")
    public  void controllerAspect() {
    }
    
    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
	 @Around("controllerAspect()")
     public Object doController(ProceedingJoinPoint joinPoint) {
    	Object result = null;
    	String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();
		Map<String, Object> map = null;
		Long start = 0L;
		Long end = 0L;
		Long time = 0L;
		String ip = null;
		try {
			map=getControllerMethodDescription(joinPoint);
			
			start = System.currentTimeMillis();
			
			result = joinPoint.proceed();
			
			end = System.currentTimeMillis();
		    
			time = end - start;

			Object[] args = joinPoint.getArgs();
	    	HttpServletRequest request = null;

	    	//通过分析aop监听参数分析出request等信息  
	        for (int i = 0; i < args.length; i++) {  
	            if (args[i] instanceof HttpServletRequest) {
	                request = (HttpServletRequest) args[i];
	                break;
	            }
	        } 
	        
	       /* ip = IpAddressUtil.getIpAddress(request);
	        AccountInfo accountInfoBean = Common.getAccountInfo(request);
        	Map<String,String> params = Common.getRequestParameters(request);
	        
        	UserOperationLogBean log = new UserOperationLogBean();
	        log.setUser_id(accountInfoBean.getId());
	        log.setParams(params.toString());
	        log.setRemote_addr(ip);
	        log.setRequest_uri(request.getRequestURL().toString());
	        log.setTitle(String.valueOf(map.get("methods")));
	        log.setCost_time(time);
	        log.setModule(String.valueOf(map.get("module")));
	        
	        new ProcessOperationLogThread(log).start();*/
	        
	        System.out.println("=====通知开始=====");
            System.out.println("请求方法:" + className + "." + methodName + "()");
            System.out.println("方法描述:" + map);
            System.out.println("请求IP:" + ip);
            /*System.out.println("请求参数:" + params.toString());*/
            System.out.println("请求耗时:" + time + " 毫秒");
            System.out.println("=====通知结束=====");
	        
		} catch (Throwable e) {
//          记录本地异常日志
			e.printStackTrace();
			logger.error("method executer error " + e.getMessage());
		}
         return result;
    }
    
    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
     @SuppressWarnings("rawtypes")
	public Map<String, Object> getControllerMethodDescription(JoinPoint joinPoint)  throws Exception {
    	 Map<String, Object> map = new HashMap<String, Object>();
    	 String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
         for (Method method : methods) {
             if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                 if (clazzs.length == arguments.length) {
                	 map.put("module", method.getAnnotation(SystemLog.class).module());
                	 map.put("methods", method.getAnnotation(SystemLog.class).methods());
                	 String de = method.getAnnotation(SystemLog.class).description();
                	 /*if(Common.isEmpty(de)) {
                         de = "执行成功";
                     }*/
                	 map.put("description", de);
                     break;
                }
            }
        }
         return map;
    }
     
    //用户操作日志保存线程
 	/*class ProcessOperationLogThread extends Thread {
 		private UserOperationLogBean userOperationLogBean;
 		public ProcessOperationLogThread(UserOperationLogBean userOperationLogBean){
 			this.userOperationLogBean = userOperationLogBean;
 		}
 		@Override
 		public void run() {
 			try {
 				userOperationLogService.save(userOperationLogBean);
 			} catch (Exception e) {
 				logger.error("process operation log Thread is save error", e);
 			}
 		}
 	}*/
}

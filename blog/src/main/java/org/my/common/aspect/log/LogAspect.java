package org.my.common.aspect.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

@Order(1)
@Aspect
@Repository
public class LogAspect {

	private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

	// 定义切入点，提供一个方法，这个方法的名字就是改切入点的id
	@Pointcut("execution(* org.my.service..*.*(..))")
	private void allMethod() {
	}

	// 针对指定的切入点表达式选择的切入点应用前置通知
	@Before("allMethod()")
	public void before(JoinPoint call) {
		Object[] args = call.getArgs();// 获取方法的参数
		Object this1 = call.getThis();// 返回代理对象
		String className = call.getTarget().getClass().getName();
		String methodName = call.getSignature().getName();
		logger.debug("【注解-前置通知】:" + className + "类的" + methodName + "方法开始了");
	}

	// 访问命名切入点来应用后置通知
	@AfterReturning("allMethod()")
	public void afterReturn() {
		logger.debug("【注解-后置通知】:方法正常结束了");
	}

	// 应用最终通知
	@After("allMethod()")
	public void after() {
		logger.debug("【注解-最终通知】:不管方法有没有正常执行完成," + "一定会返回的");
	}

	// 应用异常抛出后通知
	@AfterThrowing("allMethod()")
	public void afterThrowing() {
		logger.debug("【注解-异常抛出后通知】:方法执行时出异常了");
	}

	// 应用周围通知
	// @Around("allMethod()")
	public Object doAround(ProceedingJoinPoint call) throws Throwable {
		Object result = null;
		this.before(call);// 相当于前置通知
		try {
			result = call.proceed();
			this.afterReturn(); // 相当于后置通知
		} catch (Throwable e) {
			this.afterThrowing(); // 相当于异常抛出后通知
			throw e;
		} finally {
			this.after(); // 相当于最终通知
		}
		return result;
	}
}

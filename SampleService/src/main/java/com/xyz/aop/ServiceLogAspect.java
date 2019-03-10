package com.xyz.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class ServiceLogAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(public * com.xyz.service.*.*(..))")
    public void serviceLog(){}

    @Before("serviceLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " start...");

    }

    @After("serviceLog()")
    public  void doAfter(JoinPoint joinPoint){
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " end...");

    }

}

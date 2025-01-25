package com.template.spring.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.template.spring.util.LoggingUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

  @Autowired
  public LoggingAspect(ObjectMapper objectMapper) {
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Pointcut("execution(* com.template.spring.application..*(..))")
  public void applicationPackagePointcut() {}


  @Pointcut("execution(* com.template.spring.domain..*(..))")
  public void domainPackagePointcut() {}

  @Pointcut("execution(* com.template.spring.infrastructure..*(..))")
  public void infrastructurePackagePointcut() {}

  @Pointcut("execution(* com.template.spring.web..*(..))")
  public void primaryAdaptersPackagePointcut() {}

  @Before("applicationPackagePointcut() || domainPackagePointcut() || infrastructurePackagePointcut() || primaryAdaptersPackagePointcut()")
  public void logBeforeMethod(JoinPoint joinPoint) {
    if (notExcluded(joinPoint)) {
      log.info("Entering method: {}", joinPoint.getSignature().toShortString());
    }
  }

  @AfterReturning(pointcut = "applicationPackagePointcut() || domainPackagePointcut() || infrastructurePackagePointcut() || primaryAdaptersPackagePointcut()", returning = "result")
  public void logAfterMethod(JoinPoint joinPoint, Object result) {
    if (notExcluded(joinPoint)) {
      String maskedResultString = LoggingUtil.maskSensitiveData(result);
      log.info("Method {} executed with result: {}", joinPoint.getSignature().toShortString(), maskedResultString);
    }
  }

  @AfterThrowing(pointcut = "applicationPackagePointcut() || domainPackagePointcut() || infrastructurePackagePointcut() || primaryAdaptersPackagePointcut()", throwing = "ex")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
    if (notExcluded(joinPoint)) {
      log.error("Exception in method: {}", joinPoint.getSignature().toShortString(), ex);
    }
  }

  private boolean notExcluded(JoinPoint joinPoint) {
    return !joinPoint.getSignature().getDeclaringTypeName().contains("MapperImpl");
  }
}
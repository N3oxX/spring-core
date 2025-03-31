package com.template.spring.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    public LoggingAspect(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Combine all the pointcuts into a single pointcut expression
    @SuppressWarnings("EmptyMethod")
    @Pointcut("execution(* com.template.spring.core.application..*(..)) || " +
            "execution(* com.template.spring.core.domain..*(..)) || " +
            "execution(* com.template.spring.core.infrastructure..*(..)) || " +
            "execution(* com.template.spring.core.web..*(..)) || " +
            "execution(* com.template.spring.common.crud..*(..))")
    public void applicationMethods() {
    }

    // Use the combined pointcut for Before, AfterReturning, and AfterThrowing advice
    @Before("applicationMethods()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        if (notExcluded(joinPoint)) {
            log.info("Entering method: {}", joinPoint.getSignature().toShortString());
        }
    }

    @AfterReturning(
            pointcut = "applicationMethods()",
            returning = "result"
    )
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        if (notExcluded(joinPoint)) {
            log.info("Method {} executed with result: {}", joinPoint.getSignature().toShortString(), result);
        }
    }

    @AfterThrowing(pointcut = "applicationMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        if (notExcluded(joinPoint)) {
            log.error("Exception in method: {}", joinPoint.getSignature().toShortString(), ex);
        }
    }

    private boolean notExcluded(JoinPoint joinPoint) {
        return !joinPoint.getSignature().getDeclaringTypeName().contains("MapperImpl");
    }
}
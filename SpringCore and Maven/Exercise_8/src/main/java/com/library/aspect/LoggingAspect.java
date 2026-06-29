package com.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Aspect logging class demonstrating basic AOP advices.
 * Implements ONLY @Before and @After advices.
 */
@Aspect
public class LoggingAspect {

    // Before Advice: Runs before the target service method executes
    @Before("execution(* com.library.service.BookService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("[AOP BEFORE] Executing method: " + joinPoint.getSignature().getName());
    }

    // After Advice: Runs after the target service method executes (regardless of outcome)
    @After("execution(* com.library.service.BookService.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("[AOP AFTER] Finished method: " + joinPoint.getSignature().getName());
    }
}

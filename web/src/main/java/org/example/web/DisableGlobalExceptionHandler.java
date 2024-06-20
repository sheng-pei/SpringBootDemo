package org.example.web;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

@Aspect
@Order(-1)
public class DisableGlobalExceptionHandler {

    @Pointcut("execution(public * org.example.controller..*Controller .* (..)) && (" +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            ")")
    private void pointCut() {
    }

    @Around("pointCut()")
    public Object wrap(ProceedingJoinPoint point) throws Throwable {
        try {
            return point.proceed();
        } catch (Throwable t) {
            throw t;
        }
    }

}

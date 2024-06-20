package org.example.web;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class ServiceLogWriter {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogWriter.class);

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

    @AfterReturning(value = "pointCut()", returning = "ret")
    public void onSuccess(JoinPoint point, Object ret) {

    }

    @AfterThrowing(value = "pointCut()", throwing = "t")
    public void onException(JoinPoint point, Throwable t) {
        logger.error("Service error: " + t.getMessage(), t);
    }
}

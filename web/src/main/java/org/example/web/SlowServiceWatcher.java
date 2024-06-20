package org.example.web;

import cn.hutool.core.date.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
public class SlowServiceWatcher {
    private static final Logger logger = LoggerFactory.getLogger(SlowServiceWatcher.class);
    private static final int SLOW_SERVICE_THRESHOLD = 10;

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
    public Object wrap(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            return pjp.proceed();
        } finally {
            watch.stop();
            if (watch.getTotalTimeSeconds() >= SLOW_SERVICE_THRESHOLD) {
                logger.warn("this api method execute cost too long time, please optimize it:{} s", SLOW_SERVICE_THRESHOLD);
            }
        }
    }
}

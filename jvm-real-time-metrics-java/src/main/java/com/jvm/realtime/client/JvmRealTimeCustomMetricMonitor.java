package com.jvm.realtime.client;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;

/**
 * Class to be used for the developer API of the metrics system, allowing the user to add their own custom metrics.
 */
public abstract class JvmRealTimeCustomMetricMonitor {

    private final CounterService counterService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JvmRealTimeCustomMetricMonitor.class);

    @Autowired
    public JvmRealTimeCustomMetricMonitor(CounterService counterService) {
        this.counterService = counterService;
    }

    /**
     * Pointcuts tell AspectJ where in the code to run the aspect.
     * This one is for the custom @JvmRealTimeCustomMetric annotation.
     */
    @Pointcut("within(@com.jvm.realtime.client.JvmRealTimeCustomMetric *)")
    public void beanAnnotatedWithMonitor() {}

    // Pointcut for public methods
    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    // Pointcut for public methods also annotated with the annotation
    @Pointcut("publicMethod() && beanAnnotatedWithMonitor()")
    public void publicMethodInsideAClassMarkedWithAtMonitor() {}

    /** This is the method that is called whenever one of the pointcuts is hit. It is to be extended
     * so the application can monitor that metric whatever way they see fit.
     * @param pjp
     * @return the output of the aspect.
     * @throws Throwable
     */
    @Around("publicMethodInsideAClassMarkedWithAtMonitor()")
    public abstract Object profile(ProceedingJoinPoint pjp) throws Throwable;

}

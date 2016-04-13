package com.jvm.realtime.client;

import com.jvm.realtime.model.QueryTimeModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * AspectJ aspect for timing queries, allows the use of the @JvmRealTimeSqlMonitor custom annotation to time queries.
 */
@Aspect
@Component
public class SQLProfiler {

    @Autowired
    private Environment environment;
    private RestTemplate restTemplate = new RestTemplate();

    private static final Logger LOGGER = LoggerFactory.getLogger(SQLProfiler.class);

    /**
     * Pointcuts tell AspectJ where in the code to run the aspect.
     * This one is for the custom @JvmRealTimeSqlMonitor annotation.
     */
    @Pointcut("within(@com.jvm.realtime.client.JvmRealTimeSqlMonitor *)")
    public void beanAnnotatedWithMonitor() {}

    // Pointcut for public methods
    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    // Pointcut for public methods also annotated with the annotation
    @Pointcut("publicMethod() && beanAnnotatedWithMonitor()")
    public void publicMethodInsideAClassMarkedWithAtMonitor() {}

    /** This is the method that is called whenever one of the pointcuts is hit. It sends the query time to the
     * main JVM Real Time metrics system application.
     * @param pjp
     * @return the output of the aspect.
     * @throws Throwable
     */
    @Around("publicMethodInsideAClassMarkedWithAtMonitor()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        String applicationName = environment.getProperty("docker.image.name");
        System.out.println(pjp.getSignature().getName());
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().getName();
        long start = System.currentTimeMillis();
        LOGGER.info("Timing query for class {} and method {}", className, methodName);
        Object output = pjp.proceed();
        LOGGER.info("Query execution completed.");
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Query execution time: " + elapsedTime + " milliseconds.");
        QueryTimeModel queryTimeModel = new QueryTimeModel(
                applicationName,
                className,
                methodName,
                elapsedTime,
                System.currentTimeMillis()
        );

        String exceptionUrl = String.format("http://%s:%s/querytime/save", "localhost", 8090);

        // Send the query time object to the main JVMRT app via REST.
        try {
            LOGGER.info("Sending query information to main JVMRT application");
            restTemplate.postForObject(exceptionUrl, queryTimeModel, QueryTimeModel.class);
        } catch (RestClientException e) {
            LOGGER.error("JVMRT seems to be down at the moment, cannot send query time to main app.", e);
        }

        return output;
    }
}

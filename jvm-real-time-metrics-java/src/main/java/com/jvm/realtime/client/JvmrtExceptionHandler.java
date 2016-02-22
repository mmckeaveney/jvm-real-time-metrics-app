package com.jvm.realtime.client;

import com.jvm.realtime.model.ExceptionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A class that will be used to handle all uncaught exceptions and notify when one is thrown.
 */
public class JvmrtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JvmrtExceptionHandler.class);
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        String exceptionClass = exception.getStackTrace()[0].getClassName();
        String exceptionMethod = exception.getStackTrace()[0].getMethodName();
        String exceptionMessage = exception.getMessage();
        String exceptionType = exception.getClass().getSimpleName();
        String appName = exception.getClass().getPackage().getImplementationTitle();
        ExceptionModel thrownException = new ExceptionModel(
                exceptionMessage,
                appName,
                exceptionMethod,
                exceptionClass,
                exceptionType
        );

        LOGGER.error("Uncaught Exception thrown of type {}. Sending to JVMRT Main app for processing", exceptionType);

        String exceptionUrl = String.format("http://%s:%s/api/", "localhost", 8090);
        restTemplate.postForObject(exceptionUrl, thrownException, String.class);



    }
}

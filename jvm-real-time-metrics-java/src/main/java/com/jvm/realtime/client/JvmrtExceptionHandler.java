package com.jvm.realtime.client;

import com.jvm.realtime.model.ExceptionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A class that will be used to handle all uncaught exceptions and notify when one is thrown.
 */
@Configuration
@ControllerAdvice
@EnableAutoConfiguration
@IgnoreDuringScan
public class JvmrtExceptionHandler implements HandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(JvmrtExceptionHandler.class);

    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private Environment environment;

    // Catches exceptions in client applications and sends them to the main JVMRT app for processing.
    @ExceptionHandler(Exception.class)
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler, Exception exception) {

        // Get the relevant attributes from the exception
        String applicationName = environment.getProperty("docker.image.name");
        String exceptionMessage = exception.getMessage();
        String exceptionMethod = exception.getStackTrace()[0].getMethodName();
        String exceptionClass = exception.getStackTrace()[0].getClassName();
        String exceptionType = exception.getClass().getSimpleName();

        // Create an exception model from them with a timestamp
        ExceptionModel thrownException = new ExceptionModel(
                exceptionMessage,
                applicationName,
                exceptionMethod,
                exceptionClass,
                exceptionType,
                System.currentTimeMillis() / 1000L
        );

        LOGGER.error("Uncaught Exception thrown of type {}. Sending to JVMRT Main app for processing", thrownException.toString());

        String exceptionUrl = String.format("http://%s:%s/exception", "localhost", 8090);

        try {
            // POST the object to the JVMRT main application
            restTemplate.postForObject(exceptionUrl, thrownException, ExceptionModel.class);
        } catch (RestClientException e) {
            LOGGER.error("JVMRT seems to be down at the moment, cannot send exception to main app.", e);
        }

        return new ModelAndView();
    }
}

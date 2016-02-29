package com.jvm.realtime.client;

import com.jvm.realtime.model.ExceptionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.ConnectException;

/**
 * A class that will be used to handle all uncaught exceptions and notify when one is thrown.
 */
@ControllerAdvice
public class JvmrtExceptionHandler implements HandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(JvmrtExceptionHandler.class);
    private RestTemplate restTemplate = new RestTemplate();

    @ExceptionHandler(Exception.class)
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler, Exception exception) {

        String exceptionClass = exception.getStackTrace()[0].getClassName();
        String exceptionMethod = exception.getStackTrace()[0].getMethodName();
        String exceptionMessage = exception.getMessage();
        String exceptionType = exception.getClass().getSimpleName();
        String appName = exception.getClass().getPackage().getImplementationVersion();
        ExceptionModel thrownException = new ExceptionModel(
                exceptionMessage,
                appName,
                exceptionMethod,
                exceptionClass,
                exceptionType,
                System.currentTimeMillis()
        );

        LOGGER.error("Uncaught Exception thrown of type {}. Sending to JVMRT Main app for processing", thrownException.toString());

        String exceptionUrl = String.format("http://%s:%s/api/exception", "localhost", 8090);

        try {
            restTemplate.postForObject(exceptionUrl, thrownException, ExceptionModel.class);
        } catch (RestClientException e) {
            LOGGER.error("JVMRT seems to be down at the moment, cannot send exception to main app.", e);
        }

        return new ModelAndView();
    }
}

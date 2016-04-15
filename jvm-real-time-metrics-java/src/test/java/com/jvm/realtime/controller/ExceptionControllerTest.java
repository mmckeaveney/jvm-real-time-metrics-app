package com.jvm.realtime.controller;

import com.jvm.realtime.JvmRealTimeMetricsJavaApplication;
import com.jvm.realtime.model.DockerEvent;
import com.jvm.realtime.model.ExceptionModel;
import com.jvm.realtime.persistence.ExceptionRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class ExceptionControllerTest {

    @Autowired
    private ExceptionController exceptionController;
    @Autowired
    private ExceptionRepository exceptionRepository;

    @Before
    public void setUp() throws Exception {
        exceptionRepository.deleteAll();
    }

    @Test
    public void testHandleExceptionFromClient() throws Exception {
       // Given
        ExceptionModel exceptionModel = new ExceptionModel("message", "appname", "method", "class", "type", 0L);
        String id = exceptionModel.getExceptionType() + exceptionModel.getTime();

        // When
        exceptionController.handleExceptionFromClient(exceptionModel);

        // Then
        assertThat(exceptionRepository.findOne(id), is(exceptionModel));
        assertThat(exceptionRepository.findAll().size(), is(1));

    }

    @Test
    public void testGetAllExceptions() throws Exception {
        // Given
        ExceptionModel exceptionModel = new ExceptionModel("message", "appname", "method", "class", "type", 0L);
        exceptionRepository.save(exceptionModel);

        // When
        List<ExceptionModel> allExceptions = exceptionController.getAllExceptions();

        // Then
        assertThat(allExceptions, contains(exceptionModel));
        assertThat(allExceptions.size(), is(1));
    }

    @Test
    public void testGetMostRecentException() throws Exception {
        // Given
        ExceptionModel exceptionModel = new ExceptionModel("message", "appname", "method", "class", "type", 0L);
        ExceptionModel recentExceptionModel = new ExceptionModel("othermessage", "appname2", "method2", "class2", "type2", 100L);
        exceptionRepository.save(exceptionModel);
        exceptionRepository.save(recentExceptionModel);

        // When
        List<ExceptionModel> mostRecent = exceptionController.getMostRecentException();

        // Then
        assertThat(mostRecent, contains(recentExceptionModel));
        assertThat(mostRecent, not(contains(exceptionModel)));
        assertThat(mostRecent.size(), is(1));
        assertThat(mostRecent.get(0), is(recentExceptionModel));
    }

    @Test
    public void testGetExceptionsForApp() throws Exception {
        // Given
        ExceptionModel exceptionModel = new ExceptionModel("message", "appname", "method", "class", "type", 0L);
        ExceptionModel otherExceptionModel = new ExceptionModel("othermessage", "appname2", "method2", "class2", "type2", 100L);
        exceptionRepository.save(exceptionModel);
        exceptionRepository.save(otherExceptionModel);

        // When
        List<ExceptionModel> exceptionsForApp = exceptionController.getExceptionsForApp("appname");

        // When
        assertThat(exceptionsForApp, contains(exceptionModel));
        assertThat(exceptionsForApp, Matchers.not(contains(otherExceptionModel)));
        assertThat(exceptionsForApp.size(), is(1));
        assertThat(exceptionsForApp.get(0).getApplicationName(), is("appname"));
        assertThat(exceptionsForApp.get(0).getApplicationName(), is(Matchers.not("appname2")));


    }
}
package com.jvm.realtime.data;

import com.jvm.realtime.JvmRealTimeMetricsJavaApplication;
import com.jvm.realtime.model.ExceptionModel;
import com.jvm.realtime.persistence.ExceptionRepository;
import com.jvm.realtime.persistence.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class ExceptionProcessorIntegrationTest {

    @Autowired
    private ExceptionProcessor exceptionProcessor;
    @Autowired
    private ExceptionRepository exceptionRepository;

    @Test
    public void testNotifyAndStoreException() throws Exception {
        // Given
        ExceptionModel exceptionModel = new ExceptionModel("message", "appname", "method", "class", "type", 0L);

        // When
        exceptionProcessor.notifyAndStoreException(exceptionModel);

        // Then
        assertThat(exceptionRepository.findOne(exceptionModel.getExceptionType() + exceptionModel.getTime()), is(exceptionModel));
    }
}

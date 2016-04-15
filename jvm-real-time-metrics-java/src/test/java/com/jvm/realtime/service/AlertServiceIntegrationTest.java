package com.jvm.realtime.service;

import com.google.common.collect.Lists;
import com.jvm.realtime.JvmRealTimeMetricsJavaApplication;
import com.jvm.realtime.model.AlertModel;
import com.jvm.realtime.model.UserModel;
import com.jvm.realtime.persistence.AlertRepository;
import com.jvm.realtime.persistence.UserRepository;
import com.jvm.realtime.utils.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class AlertServiceIntegrationTest {

    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private AlertService alertService;
    @Autowired
    private UserRepository userRepository;
    private TestData testData;

    @Before
    public void setUp() throws Exception {
        this.testData = new TestData();
    }

    @Test
    public void testCheckForAlertsWhenAlertIsTriggered() throws Exception {
        // Given
        userRepository.save(new UserModel("user", "username", "email@email.com", Collections.emptySet())); // Save user to send email to if alert triggers.
        AlertModel alertModel = new AlertModel("appName", "classes", "Greater Than", 0, "username", 0L);
        String id = alertModel.getAppName() + alertModel.getMetric() + alertModel.getCondition() + alertModel.getCriteria() + alertModel.getUser();
        alertRepository.save(alertModel);

        // When
        alertService.checkForAlerts(testData.testMetrics());

        // Then
        assertThat(alertRepository.findOne(id).isTriggered(), is(true));
    }

    @Test
    public void testCheckForAlertsWhenAlertIsNotTriggered() throws Exception {
        // Given
        userRepository.save(new UserModel("user", "username", "email@email.com", Collections.emptySet())); // Save user to send email to if alert triggers.
        AlertModel alertModel = new AlertModel("appName", "classes", "Less Than", 0, "username", 0L);
        String id = alertModel.getAppName() + alertModel.getMetric() + alertModel.getCondition() + alertModel.getCriteria() + alertModel.getUser();
        alertRepository.save(alertModel);

        // When
        alertService.checkForAlerts(testData.testMetrics());

        // Then
        assertThat(alertRepository.findOne(id).isTriggered(), is(false));

    }

}

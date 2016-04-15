package com.jvm.realtime.service;

import com.google.common.collect.Lists;
import com.jvm.realtime.model.AlertModel;
import com.jvm.realtime.model.UserModel;
import com.jvm.realtime.persistence.AlertRepository;
import com.jvm.realtime.persistence.UserRepository;
import com.jvm.realtime.utils.TestData;
import com.jvm.realtime.websocket.WebSocketConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private SimpMessagingTemplate websocket;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AlertService alertService;
    private TestData testData;

    @Before
    public void setUp() throws Exception {
        this.testData = new TestData();
    }

    @Test
    public void testCheckForAlertsWhenAlertIsTriggered() throws Exception {
        // Given
        when(alertRepository.findByTriggeredIsFalse()).thenReturn(alertToTrigger());

        // When
        alertService.checkForAlerts(testData.testMetrics());

        // Then
        verify(alertRepository, atLeastOnce()).save(any(AlertModel.class));
        verify(emailService, atLeastOnce()).sendAlertEmail(any(UserModel.class), any(AlertModel.class));
        verify(websocket, atLeastOnce()).convertAndSend(anyString(), any(AlertModel.class));

    }

    @Test
    public void testCheckForAlertsWhenAlertIsNotTriggered() throws Exception {
        // Given
        when(alertRepository.findByTriggeredIsFalse()).thenReturn(alertToNotTrigger());

        // When
        alertService.checkForAlerts(testData.testMetrics());

        // Then
        verify(alertRepository, never()).save(any(AlertModel.class));
        verify(emailService, never()).sendAlertEmail(any(UserModel.class), any(AlertModel.class));
        verify(websocket, never()).convertAndSend(anyString(), any(AlertModel.class));

    }

    private List<AlertModel> alertToTrigger() {
        return Lists.newArrayList(new AlertModel("appName", "classes", "Greater Than", 0, "user", 0));
    }

    private List<AlertModel> alertToNotTrigger() {
        return Lists.newArrayList(new AlertModel("appName", "classes", "Less Than", 0, "user", 0));
    }
}
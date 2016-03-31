package com.jvm.realtime.service;

import com.jvm.realtime.model.AlertModel;
import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.persistence.AlertRepository;
import com.jvm.realtime.websocket.WebSocketConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AlertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

    private AlertRepository alertRepository;
    private EmailService emailService;
    private final SimpMessagingTemplate websocket;

    @Autowired
    public AlertService(AlertRepository alertRepository, EmailService emailService, SimpMessagingTemplate websocket) {
        this.alertRepository = alertRepository;
        this.emailService = emailService;
        this.websocket = websocket;
    }

    public void checkForAlerts(Map<String, Object> metrics) {
        List<AlertModel> currentAlerts = alertRepository.findByTriggeredIsFalse();

        for (Map.Entry<String, Object> metric: metrics.entrySet()) {
           for (AlertModel alert: currentAlerts) {
               if (metric.getKey().equals(alert.getMetric())) {
                   switch (alert.getCondition()) {
                       case "Less Than":
                           if ((Integer) metric.getValue() < alert.getCriteria()) {
                               // Store that its been triggered
                               triggerAlert(alert);
                           }
                           break;
                       case "Greater Than":
                           if ((Integer) metric.getValue() > alert.getCriteria()) {
                               // Store that its been triggered
                               triggerAlert(alert);
                           }
                           break;

                   }
               }
           }
        }
    }

    private void triggerAlert(AlertModel alert) {
        LOGGER.info("Alert Triggered" + alert.toString());
        alert.setTriggered(true);
        alert.setTimeLastTriggered(System.currentTimeMillis());
        alertRepository.save(alert);
        notifyAlertViaWebsocket(alert);
    }

    private void notifyAlertViaWebsocket(AlertModel alert) {
        websocket.convertAndSend(WebSocketConfiguration.MESSAGE_PREFIX + "/alertNotification", alert);
        LOGGER.info("Alert sent over websocket at " + System.currentTimeMillis());
    }
}

package com.jvm.realtime.service;

import com.jvm.realtime.model.AlertModel;
import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.model.UserModel;
import com.jvm.realtime.persistence.AlertRepository;
import com.jvm.realtime.persistence.UserRepository;
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

    private final AlertRepository alertRepository;
    private final EmailService emailService;
    private final SimpMessagingTemplate websocket;
    private final UserRepository userRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository, EmailService emailService,
                        SimpMessagingTemplate websocket, UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.emailService = emailService;
        this.websocket = websocket;
        this.userRepository = userRepository;
    }

    /**
     * Check each of the metrics against the current alerts in the database. If one is triggered,
     * send an email to the relevant user.
     * @param metrics the metrics to check for alerts.
     */
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

    /**
     * Trigger an alert when a metric breaks the threshold set for that alert.
     * @param alert the alert to trigger from the database.
     */
    void triggerAlert(AlertModel alert) {
        LOGGER.info("Alert Triggered" + alert.toString());
        alert.setTriggered(true);
        alert.setTimeLastTriggered(System.currentTimeMillis());
        alertRepository.save(alert);
        UserModel userToAlert = userRepository.findByUsername(alert.getUser());
        emailService.sendAlertEmail(userToAlert, alert);
        notifyAlertViaWebsocket(alert);
    }

    private void notifyAlertViaWebsocket(AlertModel alert) {
        websocket.convertAndSend(WebSocketConfiguration.MESSAGE_PREFIX + "/alertnotification", alert);
        LOGGER.info("Alert sent over websocket at " + System.currentTimeMillis());
    }
}

package com.jvm.realtime.controller;

import com.google.common.collect.Lists;
import com.jvm.realtime.client.JvmRealTimeSqlMonitor;
import com.jvm.realtime.model.AlertModel;
import com.jvm.realtime.model.DockerEvent;
import com.jvm.realtime.persistence.AlertRepository;
import com.jvm.realtime.persistence.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequestMapping(value = "/api")
@RestController
public class AlertController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertController.class);

    @Autowired
    AlertRepository alertRepository;

    @RequestMapping(value = "/alerts/all", method = RequestMethod.GET)
    public @ResponseBody List<AlertModel> getAllAlerts() {
        return alertRepository.findAll();
    }

    @RequestMapping(value = "/alerts/add", method = RequestMethod.POST)
    public @ResponseBody AlertModel saveAlert(@RequestBody AlertModel alertModel) {
        alertRepository.save(alertModel);
        LOGGER.info("Alert Saved : " + alertModel.toString());
        return alertModel;
    }

    @RequestMapping(value = "/alerts/triggered/all", method = RequestMethod.GET)
    public @ResponseBody List<AlertModel> findAllTriggeredAlerts() {
        return alertRepository.findByTriggeredIsTrue();
    }

    @RequestMapping(value = "/alerts/triggered", method = RequestMethod.GET)
    public List<AlertModel> getTriggeredAlerts(@RequestParam(value = "appName") String appName) {
        return alertRepository.findByAppNameAndTriggeredIsTrue(appName);
    }

    @RequestMapping(value = "/alerts/triggered/mostRecent", method = RequestMethod.GET)
    public List<AlertModel> getMostRecentAlert() {
        AlertModel alert = alertRepository.findTopByTriggeredIsTrueOrderByTimeLastTriggeredDesc();
        if (alert == null) {
            return Collections.emptyList();
        } else {
            return Lists.newArrayList(alert);
        }
    }

    @RequestMapping(value = "/alerts/reset/{id}", method = RequestMethod.POST)
    public HttpStatus resetAlert(@PathVariable("id") String alertId) {
        AlertModel alertToReset = alertRepository.findById(alertId);
        alertToReset.setTriggered(false);
        alertRepository.save(alertToReset);
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/alerts/delete/{id}", method = RequestMethod.POST)
    public HttpStatus deleteAlert(@PathVariable("id") String alertId) {
        alertRepository.delete(alertId);
        return HttpStatus.OK;
    }

}


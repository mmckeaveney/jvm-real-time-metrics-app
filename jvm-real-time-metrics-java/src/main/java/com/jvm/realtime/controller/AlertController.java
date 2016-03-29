package com.jvm.realtime.controller;

import com.jvm.realtime.model.AlertModel;
import com.jvm.realtime.model.DockerEvent;
import com.jvm.realtime.persistence.AlertRepository;
import com.jvm.realtime.persistence.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

        @RequestMapping(value = "/alerts/add", method = RequestMethod.POST,
                produces = MediaType.APPLICATION_JSON_VALUE,
                consumes = MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody AlertModel saveAlert(@RequestBody AlertModel alertModel) {
                try {
                        alertRepository.save(alertModel);
                        LOGGER.info("Alert Saved : " + alertModel.toString());
                } catch (Exception ex) {
                        LOGGER.error("Problem when saving alert", ex);
                }
                return alertModel;
        }
}

package com.jvm.realtime.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvm.realtime.config.Config;
import com.jvm.realtime.model.ApplicationModel;
import com.jvm.realtime.websocket.WebSocketConfiguration;
import com.spotify.docker.client.messages.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@Component
public class MetricsEndpointPoller implements DataPoller {

    private RestTemplate restTemplate;
    private DockerPoller dockerPoller;
    private final SimpMessagingTemplate websocket;

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsEndpointPoller.class);

    @Autowired
    public MetricsEndpointPoller(DockerPoller dockerPoller, SimpMessagingTemplate websocket) {
        this.restTemplate = new RestTemplate();
        this.dockerPoller = dockerPoller;
        this.websocket = websocket;
    }

    public void poll() {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
               retrieveActuatorMetricsFromDockerHosts();
            }
        }, new Date(), 3000);
    }

    private void retrieveActuatorMetricsFromDockerHosts() {
        for (Container container : this.dockerPoller.getCurrentContainers()) {

            ApplicationModel currentAppModel = getApplicationMetaData(container);

            // Do the HTTP request to get metrics from spring boot actuator.
            String metricsUrl = String.format("http://%s:%s/metrics", Config.dockerHost, currentAppModel.getPublicPort());
            Map metricsMap = restTemplate.getForObject(metricsUrl, Map.class);
            currentAppModel.setActuatorMetrics(metricsMap);

            websocket.convertAndSend(WebSocketConfiguration.MESSAGE_PREFIX + "/metricsUpdate", currentAppModel);

        }
    }

    private ApplicationModel getApplicationMetaData(Container container) {
        ObjectMapper mapper = new ObjectMapper();
        ApplicationModel currentAppModel = new ApplicationModel();

        try {
            String jsonString = mapper.writeValueAsString(container);
            JsonNode rootNode = mapper.readTree(new StringReader(jsonString));

            Integer publicPort = rootNode.get("Ports").get(0).get("PublicPort").asInt();
            String appName = rootNode.get("Image").asText();

            currentAppModel.setPublicPort(publicPort);
            currentAppModel.setAppName(appName);

            return currentAppModel;

        } catch (IOException e) {
            LOGGER.error("Error parsing JSON", e);
        }

        return null;
    }

}

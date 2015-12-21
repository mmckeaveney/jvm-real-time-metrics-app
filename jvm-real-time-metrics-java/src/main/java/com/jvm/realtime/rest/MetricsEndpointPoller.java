package com.jvm.realtime.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvm.realtime.config.Config;
import com.spotify.docker.client.messages.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class MetricsEndpointPoller implements DataPoller {

    private RestTemplate restTemplate;
    private DockerPoller dockerPoller;

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsEndpointPoller.class);

    @Autowired
    public MetricsEndpointPoller(DockerPoller dockerPoller) {
        this.restTemplate = new RestTemplate();
        this.dockerPoller = dockerPoller;
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

            String endpointPort = getApplicationMetricsEndpoint(container);
            String metricsUrl = String.format("http://%s:%s/metrics", Config.dockerHost, endpointPort);
            String response = restTemplate.getForObject(metricsUrl, String.class);
            LOGGER.info(response);

        }
    }

    private String getApplicationMetricsEndpoint(Container container) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonString = mapper.writeValueAsString(container);
            JsonNode rootNode = mapper.readTree(new StringReader(jsonString));
            JsonNode portNode = rootNode.get("Ports").get(0).get("PublicPort");
            return portNode.asText();

        } catch (IOException e) {
            LOGGER.error("Error parsing JSON", e);
        }

        return null;
    }

}

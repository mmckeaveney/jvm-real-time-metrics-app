package com.jvm.realtime.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvm.realtime.config.Config;
import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.persistence.ClientAppSnapshotRepository;
import com.jvm.realtime.websocket.WebSocketConfiguration;
import com.spotify.docker.client.messages.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class MetricsEndpointPoller implements DataPoller {

    private RestTemplate restTemplate;
    private DockerPoller dockerPoller;
    private final SimpMessagingTemplate websocket;
    private ClientAppSnapshotRepository clientAppSnapshotRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsEndpointPoller.class);

    @Autowired
    public MetricsEndpointPoller(DockerPoller dockerPoller, SimpMessagingTemplate websocket,
                                 ClientAppSnapshotRepository clientAppSnapshotRepository) {
        this.restTemplate = new RestTemplate();
        this.dockerPoller = dockerPoller;
        this.websocket = websocket;
        this.clientAppSnapshotRepository = clientAppSnapshotRepository;
    }

    public void poll() {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                retrieveActuatorMetricsFromDockerHosts();
            }
        }, new Date(), 3000);
    }

    /**
     * Retrieve spring boot actuator metrics from the docker hosts and send them over the websocket to be used by the
     * client.
     */
    private void retrieveActuatorMetricsFromDockerHosts() {
        Set<ClientAppSnapshot> currentClientAppSnapshots = new HashSet<>();

        for (Container container : this.dockerPoller.getCurrentContainers()) {

            ClientAppSnapshot currentAppModel = getApplicationMetaData(container);

            // Do the HTTP request to get metrics from spring boot actuator.
            String metricsUrl = String.format("http://%s:%s/metrics", Config.dockerHost, currentAppModel.getPublicPort());


            try {
                Map<String, Object> metricsMap = restTemplate.getForObject(metricsUrl, Map.class);
                Map<String, Object> formattedMetricsMap = new HashMap<>();

                for (Map.Entry<String, Object> metric : metricsMap.entrySet()) {
                    formattedMetricsMap.put(metric.getKey().replace(".", ""), metric.getValue());
                }

                currentAppModel.setActuatorMetrics(formattedMetricsMap);
                clientAppSnapshotRepository.save(currentAppModel);

                currentClientAppSnapshots.add(currentAppModel);
            } catch (RestClientException e) {
                LOGGER.error("Error connecting to docker host at " + metricsUrl, e);
            }
        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(cal.getTime());

        if (!currentClientAppSnapshots.isEmpty()) {
            LOGGER.info("Application snapshots being transmitted over websocket");
            websocket.convertAndSend(WebSocketConfiguration.MESSAGE_PREFIX + "/metricsUpdate", currentClientAppSnapshots);
            LOGGER.info("Application snapshots transmitted over websocket at " + currentTime);
        } else {
            LOGGER.warn("No docker hosts currently available at " + currentTime);
        }
    }

    private ClientAppSnapshot getApplicationMetaData(Container container) {
        ObjectMapper mapper = new ObjectMapper();
        ClientAppSnapshot currentAppModel = new ClientAppSnapshot();

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

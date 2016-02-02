package com.jvm.realtime.rest;

import com.github.dockerjava.api.model.Container;
import com.jvm.realtime.config.Config;
import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.websocket.WebSocketConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
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
                Set<ClientAppSnapshot> currentApplicationMetrics = retrieveActuatorMetricsFromDockerHosts();
                transmitLatestSnapshotOverWebsocket(currentApplicationMetrics);
            }
        }, new Date(), 3000);
    }

    /**
     * Retrieve spring boot actuator metrics from the docker hosts and send them over the websocket to be used by the
     * client.
     */
    public Set<ClientAppSnapshot> retrieveActuatorMetricsFromDockerHosts() {
        Set<ClientAppSnapshot> currentClientAppSnapshots = new HashSet<>();

        for (Container container : this.dockerPoller.getCurrentContainers()) {

            ClientAppSnapshot currentAppModel = this.dockerPoller.getDockerApplicationMetaData(container);

            // Do the HTTP request to get metrics from spring boot actuator.
            String metricsUrl = String.format("http://%s:%s/metrics", Config.dockerHost, currentAppModel.getPublicPort());


            try {
                Map<String, Object> metricsMap = restTemplate.getForObject(metricsUrl, Map.class);
                Map<String, Object> formattedMetricsMap = new HashMap<>();

                // Mongo doesn't accept dots in map keys, this loop simply removes them.
                for (Map.Entry<String, Object> metric: metricsMap.entrySet()) {
                    formattedMetricsMap.put(metric.getKey().replace(".", ""), metric.getValue());
                }

                currentAppModel.setActuatorMetrics(formattedMetricsMap);

                currentClientAppSnapshots.add(currentAppModel);

            } catch (RestClientException e) {
                LOGGER.error("Error connecting to docker host at " + metricsUrl, e);
            }
        }

        return currentClientAppSnapshots;
    }

    private void transmitLatestSnapshotOverWebsocket(Set<ClientAppSnapshot> currentClientAppSnapshots) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(cal.getTime());

        if (!currentClientAppSnapshots.isEmpty()) {
            LOGGER.info("Real time application snapshots being transmitted over websocket");
            websocket.convertAndSend(WebSocketConfiguration.MESSAGE_PREFIX + "/metricsUpdate", currentClientAppSnapshots);
            LOGGER.info("Real time application snapshots transmitted over websocket at " + currentTime);
        } else {
            LOGGER.warn("No docker hosts currently available at " + currentTime);
        }
    }



}

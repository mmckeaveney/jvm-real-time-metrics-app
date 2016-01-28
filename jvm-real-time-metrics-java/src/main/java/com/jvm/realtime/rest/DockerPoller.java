package com.jvm.realtime.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvm.realtime.config.Config;
import com.jvm.realtime.model.ClientAppSnapshot;
import com.spotify.docker.client.*;
import com.spotify.docker.client.messages.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

@Component
public class DockerPoller implements DataPoller {


    private DockerClient dockerClient;
    private List<Container> currentContainers;
    private final SimpMessagingTemplate websocket;

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPoller.class);

    @Autowired
    public DockerPoller(SimpMessagingTemplate websocket) {
        this.dockerClient = DefaultDockerClient.builder()
                .uri(URI.create("http://" + Config.dockerHost + ":2376"))
//                .dockerCertificates(sslAuth(Config.dockerCertsPath))
                .build();
        this.currentContainers = Collections.emptyList();
        this.websocket = websocket;
    }

    public void poll() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                fetchCurrentContainers();
            }
        }, 0, 60000);

    }

    public ClientAppSnapshot getDockerApplicationMetaData(Container container) {
        ObjectMapper mapper = new ObjectMapper();
        ClientAppSnapshot currentAppModel = new ClientAppSnapshot();

        try {
            String jsonString = mapper.writeValueAsString(container);
            JsonNode rootNode = mapper.readTree(new StringReader(jsonString));

            Integer publicPort = rootNode.get("Ports").get(0).get("PublicPort").asInt();
            String appName = rootNode.get("Image").asText();

            currentAppModel.setPublicPort(publicPort);
            currentAppModel.setAppName(appName);
            currentAppModel.setTimeStamp(System.currentTimeMillis());

            return currentAppModel;

        } catch (IOException e) {
            LOGGER.error("Error parsing JSON", e);
        }

        return null;
    }

    private List<Container> fetchCurrentContainers() {
        try {
            setCurrentContainers(this.dockerClient.listContainers());
        } catch (InterruptedException | DockerException e) {
            LOGGER.error("Error when polling docker containers endpoint.", e);
        }

        return getCurrentContainers();
    }

    private DockerCertificates sslAuth(String absoluteCertPath) {

        DockerCertificates dockerAuth = null;

        try {
            dockerAuth = new DockerCertificates(Paths.get(absoluteCertPath));
        } catch (DockerCertificateException e) {
            LOGGER.error("Error authenticating docker machine with SSL.");
        }
        return dockerAuth;
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    public List<Container> getCurrentContainers() {
        return currentContainers;
    }

    public void setCurrentContainers(List<Container> currentContainers) {
        this.currentContainers = currentContainers;
    }
}

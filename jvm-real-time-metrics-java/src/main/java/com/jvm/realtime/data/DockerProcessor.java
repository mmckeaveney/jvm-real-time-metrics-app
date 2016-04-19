package com.jvm.realtime.data;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.jvm.realtime.config.Config;
import com.jvm.realtime.config.ConfigurationProps;
import com.jvm.realtime.model.ClientAppSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DockerProcessor implements DataProcessor {


    private DockerClient dockerClient;
    private List<Container> currentContainers;
    private ConfigurationProps configurationProps;

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerProcessor.class);

    /**
     * Constructor.
     * @param configurationProps the dynamic configuration properties for connecting to docker.
     */
    @Autowired
    public DockerProcessor(ConfigurationProps configurationProps) {
        this.configurationProps = configurationProps;
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                .withVersion("1.18")
                .withUri(String.format("http://%s:%s", configurationProps.getDockerHost(), configurationProps.getDockerPort()))
                .build();
        this.dockerClient = DockerClientBuilder.getInstance(config).build();
        this.currentContainers = Collections.emptyList();
    }

    public void poll() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                fetchCurrentContainers();
            }
        }, 0, 5000);

    }

    /**
     * Get all the information required from the docker containers to create application snapshots to
     * be stored in MongoDB.
     * @param container The docker container to extract information from.
     * @return The ClientAppSnapshot object created from the docker container.
     */
    public ClientAppSnapshot getDockerApplicationMetaData(Container container) {
        ClientAppSnapshot currentAppModel = new ClientAppSnapshot();

        Integer publicPort = container.getPorts()[0].getPublicPort();
        String appName = container.getImage();

        currentAppModel.setContainerId(container.getId());
        currentAppModel.setPublicPort(publicPort);
        currentAppModel.setAppName(appName);
        currentAppModel.setTimeStamp(System.currentTimeMillis());

        return currentAppModel;
    }

    public void killApp(String containerId) {
        LOGGER.info("Killing docker app with container ID {}", containerId);
        dockerClient.killContainerCmd(containerId).exec();
    }

    public void restartApp(String containerId) {
        LOGGER.info("Restarting docker app with container ID {}", containerId);
        dockerClient.restartContainerCmd(containerId).exec();
    }

    /**
     * Get all the current docker containers running in the docker environment and update the current list.
     * @return The list of docker containers currently running.
     */
    private List<Container> fetchCurrentContainers() {
        try {
            setCurrentContainers(this.dockerClient.listContainersCmd().exec());
        } catch (Exception e) {
            LOGGER.error("Error when polling docker containers endpoint.", e);
        }

        return getCurrentContainers();
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

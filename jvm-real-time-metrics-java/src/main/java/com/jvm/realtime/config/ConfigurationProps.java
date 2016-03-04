package com.jvm.realtime.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//@ConfigurationProperties
public class ConfigurationProps {

    private String dockerHost = "localhost";
    private int dockerPort = 2376;
    private String dockerCertsPath = "/Users/martinmckeaveney/.docker/machine/certs";

    public String getDockerHost() {
        return dockerHost;
    }

    public void setDockerHost(String dockerHost) {
        this.dockerHost = dockerHost;
    }

    public int getDockerPort() {
        return dockerPort;
    }

    public void setDockerPort(int dockerPort) {
        this.dockerPort = dockerPort;
    }

    public String getDockerCertsPath() {
        return dockerCertsPath;
    }

    public void setDockerCertsPath(String dockerCertsPath) {
        this.dockerCertsPath = dockerCertsPath;
    }
}

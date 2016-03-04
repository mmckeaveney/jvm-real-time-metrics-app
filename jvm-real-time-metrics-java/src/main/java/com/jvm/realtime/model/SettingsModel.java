package com.jvm.realtime.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class SettingsModel {

    private String dockerHost;
    private int dockerPort;
    public enum Theme {
        DARK_THEME,
        LIGHT_THEME
    }

    public SettingsModel(String dockerHost, int dockerPort) {
        this.dockerHost = dockerHost;
        this.dockerPort = dockerPort;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingsModel that = (SettingsModel) o;
        return Objects.equal(dockerHost, that.dockerHost) &&
                Objects.equal(dockerPort, that.dockerPort);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dockerHost, dockerPort);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dockerHost", dockerHost)
                .add("dockerPort", dockerPort)
                .toString();
    }
}

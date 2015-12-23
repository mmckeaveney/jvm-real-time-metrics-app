package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.Map;

public class ApplicationModel {

    @JsonProperty("appName")
    private String appName;

    @JsonProperty("actuatorMetrics")
    private Map<String, Object> actuatorMetrics;

    @JsonProperty("publicPort")
    private Integer publicPort;

    public ApplicationModel() {
    }

    public ApplicationModel(String appName, Map<String, Object> metrics, Integer publicPort) {
        this.appName = appName;
        this.actuatorMetrics = metrics;
        this.publicPort = publicPort;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Map<String, Object> getActuatorMetrics() {
        return actuatorMetrics;
    }

    public void setActuatorMetrics(Map<String, Object> actuatorMetrics) {
        this.actuatorMetrics = actuatorMetrics;
    }

    public Integer getPublicPort() {
        return publicPort;
    }

    public void setPublicPort(Integer publicPort) {
        this.publicPort = publicPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationModel that = (ApplicationModel) o;
        return Objects.equal(appName, that.appName) &&
                Objects.equal(actuatorMetrics, that.actuatorMetrics) &&
                Objects.equal(publicPort, that.publicPort);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(appName, actuatorMetrics, publicPort);
    }
}

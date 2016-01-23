package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

import java.util.Map;

public class ClientAppSnapshot {

    @Id
    private String id;

    @JsonProperty("appName")
    private String appName;

    @JsonProperty("actuatorMetrics")
    private Map<String, Object> actuatorMetrics;

    @JsonProperty("publicPort")
    private Integer publicPort;

    public ClientAppSnapshot() {
    }

    public ClientAppSnapshot(String id, String appName, Map<String, Object> actuatorMetrics, Integer publicPort) {
        this.id = id;
        this.appName = appName;
        this.actuatorMetrics = actuatorMetrics;
        this.publicPort = publicPort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        ClientAppSnapshot that = (ClientAppSnapshot) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(appName, that.appName) &&
                Objects.equal(actuatorMetrics, that.actuatorMetrics) &&
                Objects.equal(publicPort, that.publicPort);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, appName, actuatorMetrics, publicPort);
    }

    @Override
    public String toString() {
        return "ClientAppSnapshot{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", actuatorMetrics=" + actuatorMetrics +
                ", publicPort=" + publicPort +
                '}';
    }
}

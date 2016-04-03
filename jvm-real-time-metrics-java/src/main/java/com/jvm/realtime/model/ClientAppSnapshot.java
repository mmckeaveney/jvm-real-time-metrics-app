package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.Event;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

public class ClientAppSnapshot {

    @Id
    private String id;

    @JsonProperty("containerId")
    private String containerId;

    @JsonProperty("appName")
    private String appName;

    @JsonProperty("actuatorMetrics")
    private Map<String, Object> actuatorMetrics;

    @JsonProperty("publicPort")
    private Integer publicPort;

    @JsonProperty("timeStamp")
    private Long timeStamp;

    @JsonProperty("applicationEvents")
    private List<Event> applicationEvents;

    public ClientAppSnapshot() {
    }

    public ClientAppSnapshot(String id, String containerId, String appName, Map<String, Object> actuatorMetrics,
                             Integer publicPort, Long timeStamp, List<Event> applicationEvents) {
        this.id = id;
        this.containerId = containerId;
        this.appName = appName;
        this.actuatorMetrics = actuatorMetrics;
        this.publicPort = publicPort;
        this.timeStamp = timeStamp;
        this.applicationEvents = applicationEvents;
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

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Event> getApplicationEvents() {
        return applicationEvents;
    }

    public void setApplicationEvents(List<Event> applicationEvents) {
        this.applicationEvents = applicationEvents;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("containerId", containerId)
                .add("appName", appName)
                .add("actuatorMetrics", actuatorMetrics)
                .add("publicPort", publicPort)
                .add("timeStamp", timeStamp)
                .add("applicationEvents", applicationEvents)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientAppSnapshot that = (ClientAppSnapshot) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(containerId, that.containerId) &&
                Objects.equal(appName, that.appName) &&
                Objects.equal(actuatorMetrics, that.actuatorMetrics) &&
                Objects.equal(publicPort, that.publicPort) &&
                Objects.equal(timeStamp, that.timeStamp) &&
                Objects.equal(applicationEvents, that.applicationEvents);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, containerId, appName, actuatorMetrics, publicPort, timeStamp, applicationEvents);
    }
}

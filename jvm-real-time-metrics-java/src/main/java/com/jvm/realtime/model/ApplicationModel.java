package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.Map;

public class ApplicationModel {

    @JsonProperty("appName")
    private String appName;

    @JsonProperty("metrics")
    private Map<String, Object> metrics;

    public ApplicationModel(String appName, Map metrics) {
        this.appName = appName;
        this.metrics = metrics;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Map<String, Object> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<String, Object> metrics) {
        this.metrics = metrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationModel that = (ApplicationModel) o;
        return Objects.equal(appName, that.appName) &&
                Objects.equal(metrics, that.metrics);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(appName, metrics);
    }
}

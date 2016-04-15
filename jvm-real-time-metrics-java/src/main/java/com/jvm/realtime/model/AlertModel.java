package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

public class AlertModel {

    private String id;
    @JsonProperty("appName")
    private String appName;
    @JsonProperty("metric")
    private String metric;
    @JsonProperty("condition")
    private String condition;
    @JsonProperty("criteria")
    private int criteria;
    @JsonProperty("user")
    private String user;
    @JsonProperty("triggered")
    private boolean triggered;
    private long timeLastTriggered;

    public AlertModel() {
    }

    public AlertModel(String appName, String metric, String condition, int criteria, String user, long timeLastTriggered) {
        this.id = appName + metric + condition + criteria + user;
        this.appName = appName;
        this.metric = metric;
        this.condition = condition;
        this.criteria = criteria;
        this.user = user;
        this.triggered = false;
        this.timeLastTriggered = timeLastTriggered;
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

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getCriteria() {
        return criteria;
    }

    public void setCriteria(int criteria) {
        this.criteria = criteria;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public long getTimeLastTriggered() {
        return timeLastTriggered;
    }

    public void setTimeLastTriggered(long timeLastTriggered) {
        this.timeLastTriggered = timeLastTriggered;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("appName", appName)
                .add("metric", metric)
                .add("condition", condition)
                .add("criteria", criteria)
                .add("user", user)
                .add("triggered", triggered)
                .add("timeLastTriggered", timeLastTriggered)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertModel that = (AlertModel) o;
        return criteria == that.criteria &&
                triggered == that.triggered &&
                timeLastTriggered == that.timeLastTriggered &&
                Objects.equal(id, that.id) &&
                Objects.equal(appName, that.appName) &&
                Objects.equal(metric, that.metric) &&
                Objects.equal(condition, that.condition) &&
                Objects.equal(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, appName, metric, condition, criteria, user, triggered, timeLastTriggered);
    }

}

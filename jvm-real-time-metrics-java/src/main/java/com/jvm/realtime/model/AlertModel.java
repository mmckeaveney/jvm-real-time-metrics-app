package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

public class AlertModel {

    @Id
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

    public AlertModel() {
    }

    public AlertModel(String appName, String metric, String condition, int criteria, String user) {
        this.appName = appName;
        this.metric = metric;
        this.condition = condition;
        this.criteria = criteria;
        this.user = user;
    }

    public String getAppName() {
        return appName;
    }

    public String getMetric() {
        return metric;
    }

    public String getCondition() {
        return condition;
    }

    public int getCriteria() {
        return criteria;
    }

    public String getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertModel that = (AlertModel) o;
        return criteria == that.criteria &&
                Objects.equal(appName, that.appName) &&
                Objects.equal(metric, that.metric) &&
                Objects.equal(condition, that.condition) &&
                Objects.equal(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(appName, metric, condition, criteria, user);
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
                .toString();
    }
}

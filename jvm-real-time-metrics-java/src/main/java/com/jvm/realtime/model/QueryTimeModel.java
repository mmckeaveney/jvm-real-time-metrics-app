package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

public class QueryTimeModel {

    @Id
    private String id;
    @JsonProperty("applicationName")
    private String applicationName;
    @JsonProperty("className")
    private String className;
    @JsonProperty("methodName")
    private String methodName;
    @JsonProperty("executionTime")
    private long executionTime;
    @JsonProperty("timeExecuted")
    private long timeExecuted;

    public QueryTimeModel() {}

    public QueryTimeModel(String applicationName, String className, String methodName, long executionTime, long timeExecuted) {
        this.id = applicationName + className + methodName + timeExecuted;
        this.applicationName = applicationName;
        this.className = className;
        this.methodName = methodName;
        this.executionTime = executionTime;
        this.timeExecuted = timeExecuted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public long getTimeExecuted() {
        return timeExecuted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryTimeModel that = (QueryTimeModel) o;
        return executionTime == that.executionTime &&
                timeExecuted == that.timeExecuted &&
                Objects.equal(applicationName, that.applicationName) &&
                Objects.equal(className, that.className) &&
                Objects.equal(methodName, that.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(applicationName, className, methodName, executionTime, timeExecuted);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("applicationName", applicationName)
                .add("className", className)
                .add("methodName", methodName)
                .add("executionTime", executionTime)
                .add("timeExecuted", timeExecuted)
                .toString();
    }
}

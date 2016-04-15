package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

/**
 * A class representing an exception thrown by any of the client applications.
 */
public class ExceptionModel {

    private String id;
    @JsonProperty("exceptionType")
    private String exceptionType;
    @JsonProperty("exceptionClass")
    private String exceptionClass;
    @JsonProperty("exceptionMethod")
    private String exceptionMethod;
    @JsonProperty("exceptionMessage")
    private String exceptionMessage;
    @JsonProperty("applicationName")
    private String applicationName;
    @JsonProperty("time")
    private Long time;


    public ExceptionModel() {}

    public ExceptionModel(String exceptionMessage,
                          String applicationName,
                          String exceptionMethod,
                          String exceptionClass,
                          String exceptionType,
                          Long time) {
        this.id = exceptionType + time;
        this.exceptionMessage = exceptionMessage;
        this.applicationName = applicationName;
        this.exceptionMethod = exceptionMethod;
        this.exceptionClass = exceptionClass;
        this.exceptionType = exceptionType;
        this.time = time;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionMethod() {
        return exceptionMethod;
    }

    public void setExceptionMethod(String exceptionMethod) {
        this.exceptionMethod = exceptionMethod;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionModel that = (ExceptionModel) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(exceptionType, that.exceptionType) &&
                Objects.equal(exceptionClass, that.exceptionClass) &&
                Objects.equal(exceptionMethod, that.exceptionMethod) &&
                Objects.equal(exceptionMessage, that.exceptionMessage) &&
                Objects.equal(applicationName, that.applicationName) &&
                Objects.equal(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, exceptionType, exceptionClass, exceptionMethod, exceptionMessage, applicationName, time);
    }

    @Override
    public String toString() {
        return "ExceptionModel{" +
                "id='" + id + '\'' +
                ", exceptionType='" + exceptionType + '\'' +
                ", exceptionClass='" + exceptionClass + '\'' +
                ", exceptionMethod='" + exceptionMethod + '\'' +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", time=" + time +
                '}';
    }
}

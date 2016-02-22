package com.jvm.realtime.model;

import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

/**
 * A class representing an exception thrown by any of the client applications.
 */
public class ExceptionModel {

    @Id
    private String id;
    private final String exceptionType;
    private final String exceptionClass;
    private final String exceptionMethod;
    private final String exceptionMessage;
    private final String applicationName;


    public ExceptionModel(String exceptionMessage, String applicationName, String exceptionMethod, String exceptionClass, String exceptionType) {
        this.exceptionMessage = exceptionMessage;
        this.applicationName = applicationName;
        this.exceptionMethod = exceptionMethod;
        this.exceptionClass = exceptionClass;
        this.exceptionType = exceptionType;
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

    public String getExceptionClass() {
        return exceptionClass;
    }

    public String getExceptionMethod() {
        return exceptionMethod;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public String getApplicationName() {
        return applicationName;
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
                Objects.equal(applicationName, that.applicationName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, exceptionType, exceptionClass, exceptionMethod, exceptionMessage, applicationName);
    }
}

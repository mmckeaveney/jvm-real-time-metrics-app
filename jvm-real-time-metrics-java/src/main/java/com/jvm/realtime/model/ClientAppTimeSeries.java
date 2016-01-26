package com.jvm.realtime.model;

import com.google.common.base.Objects;

import java.util.List;
import java.util.Map;

public class ClientAppTimeSeries {

    private final Map<String, List<Object>> timeSeriesMetrics;

    public ClientAppTimeSeries(Map<String, List<Object>> timeSeriesMetrics) {
        this.timeSeriesMetrics = timeSeriesMetrics;
    }

    public Map<String, List<Object>> getTimeSeriesMetrics() {
        return timeSeriesMetrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientAppTimeSeries that = (ClientAppTimeSeries) o;
        return Objects.equal(timeSeriesMetrics, that.timeSeriesMetrics);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timeSeriesMetrics);
    }

    @Override
    public String toString() {
        return "ClientAppTimeSeries{" +
                "timeSeriesMetrics=" + timeSeriesMetrics +
                '}';
    }
}

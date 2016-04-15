package com.jvm.realtime.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;
import java.util.Map;

public class ClientAppTimeSeries {

    private final Map<String, List<Object>> timeSeriesMetrics;
    private final List<ClientAppSnapshot> snapshots;

    public ClientAppTimeSeries(Map<String, List<Object>> timeSeriesMetrics, List<ClientAppSnapshot> snapshots) {
        this.timeSeriesMetrics = timeSeriesMetrics;
        this.snapshots = snapshots;
    }

    public Map<String, List<Object>> getTimeSeriesMetrics() {
        return timeSeriesMetrics;
    }

    public List<ClientAppSnapshot> getSnapshots() {
        return snapshots;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("timeSeriesMetrics", timeSeriesMetrics)
                .add("snapshots", snapshots)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientAppTimeSeries that = (ClientAppTimeSeries) o;
        return Objects.equal(timeSeriesMetrics, that.timeSeriesMetrics) &&
                Objects.equal(snapshots, that.snapshots);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timeSeriesMetrics, snapshots);
    }

}

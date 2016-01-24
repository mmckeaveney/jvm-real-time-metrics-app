package com.jvm.realtime.rest;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Set;

@Component
public class RealTimeDataClient implements InitializingBean {

    private Set<DataPoller> dataCollectors;
    private final DockerPoller dockerPoller;
    private final MetricsEndpointPoller metricsEndpointPoller;
    private final TimeSeriesMetricsPoller timeSeriesMetricsPoller;

    @Autowired
    public RealTimeDataClient(DockerPoller dockerPoller,
                              MetricsEndpointPoller metricsEndpointPoller,
                              TimeSeriesMetricsPoller timeSeriesMetricsPoller) {
        this.dockerPoller = dockerPoller;
        this.metricsEndpointPoller = metricsEndpointPoller;
        this.timeSeriesMetricsPoller = timeSeriesMetricsPoller;

    }

    public void beginCollecting() {
        this.dataCollectors = Sets.newHashSet(dockerPoller, metricsEndpointPoller, timeSeriesMetricsPoller);

        for (DataPoller poller : this.dataCollectors) {
            poller.poll();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        beginCollecting();
    }
}

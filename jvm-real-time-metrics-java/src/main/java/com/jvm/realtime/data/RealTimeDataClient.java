package com.jvm.realtime.data;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RealTimeDataClient implements InitializingBean {

    private Set<DataProcessor> dataCollectors;
    private final DockerProcessor dockerPoller;
    private final MetricsEndpointProcessor metricsEndpointPoller;
    private final TimeSeriesMetricsProcessor timeSeriesMetricsPoller;
    private final EventProcessor eventPoller;

    @Autowired
    public RealTimeDataClient(DockerProcessor dockerPoller,
                              MetricsEndpointProcessor metricsEndpointPoller,
                              TimeSeriesMetricsProcessor timeSeriesMetricsPoller,
                              EventProcessor eventPoller) {
        this.dockerPoller = dockerPoller;
        this.metricsEndpointPoller = metricsEndpointPoller;
        this.timeSeriesMetricsPoller = timeSeriesMetricsPoller;
        this.eventPoller = eventPoller;

    }

    public void beginCollecting() {
        this.dataCollectors = Sets.newHashSet(
                dockerPoller,
                metricsEndpointPoller,
                timeSeriesMetricsPoller,
                eventPoller
        );

        for (DataProcessor poller : this.dataCollectors) {
            poller.poll();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        beginCollecting();
    }
}

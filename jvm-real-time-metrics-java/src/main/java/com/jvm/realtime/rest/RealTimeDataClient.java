package com.jvm.realtime.rest;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RealTimeDataClient implements InitializingBean {

    private Set<DataPoller> dataCollectors;
    private DockerPoller dockerPoller;
    private MetricsEndpointPoller metricsEndpointPoller;

    @Autowired
    public RealTimeDataClient(Set<DataPoller> dataCollectors,
                              DockerPoller dockerPoller,
                              MetricsEndpointPoller metricsEndpointPoller) {
        this.dockerPoller = dockerPoller;
        this.metricsEndpointPoller = metricsEndpointPoller;
        this.dataCollectors = Sets.newHashSet(dockerPoller, metricsEndpointPoller);
    }

    public void beginCollecting() {
        for (DataPoller poller : this.dataCollectors) {
            poller.poll();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        beginCollecting();
    }
}

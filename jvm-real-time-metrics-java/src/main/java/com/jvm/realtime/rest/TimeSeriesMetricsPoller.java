package com.jvm.realtime.rest;

import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.persistence.ClientAppSnapshotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class TimeSeriesMetricsPoller implements DataPoller {

    private MetricsEndpointPoller metricsEndpointPoller;
    private ClientAppSnapshotRepository clientAppSnapshotRepository;


    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSeriesMetricsPoller.class);

    @Autowired
    public TimeSeriesMetricsPoller(MetricsEndpointPoller metricsEndpointPoller,
                                   ClientAppSnapshotRepository clientAppSnapshotRepository) {
        this.metricsEndpointPoller = metricsEndpointPoller;
        this.clientAppSnapshotRepository = clientAppSnapshotRepository;
    }

    /**
     * Saves a snapshot of the current metrics once every day for timeseries data.
     */
    public void poll() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Set<ClientAppSnapshot> currentApplicationMetrics = metricsEndpointPoller.retrieveActuatorMetricsFromDockerHosts();
                saveDailyMetricsSnapshot(currentApplicationMetrics);
            }
        }, 1000, 24*60*60*1000);
    }

    private void saveDailyMetricsSnapshot(Set<ClientAppSnapshot> currentClientAppSnapshots) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(cal.getTime());

        if (!currentClientAppSnapshots.isEmpty()) {
            LOGGER.info("Daily Time Series Application snapshots being saved at " + currentTime);
            clientAppSnapshotRepository.save(currentClientAppSnapshots);
            LOGGER.info("Daily Time Series application snapshots saved at " + currentTime);
        } else {
            LOGGER.warn("No docker host data currently available to save at " + currentTime);
        }
    }



}


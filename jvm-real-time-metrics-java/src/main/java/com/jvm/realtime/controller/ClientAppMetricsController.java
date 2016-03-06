package com.jvm.realtime.controller;

import com.google.common.collect.Lists;
import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.model.ClientAppTimeSeries;
import com.jvm.realtime.persistence.ClientAppSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/api")
public class ClientAppMetricsController {

    @Autowired
    ClientAppSnapshotRepository clientAppSnapshotRepository;

    @RequestMapping(value = "/timeseries", method = RequestMethod.GET)
    public ClientAppTimeSeries getTimeSeriesDataForSingleApp(@RequestParam(value = "appName") String appName,
                                                     @RequestParam(value = "timeScale") String timeScale) {
        List<ClientAppSnapshot> queryResults;
        switch (timeScale) {
            case "week":
                queryResults = clientAppSnapshotRepository.findTop7ByAppName(appName, new Sort(Sort.Direction.DESC, "time"));
                return getTimeSeriesFromQuery(queryResults);
            case "month":
                queryResults = clientAppSnapshotRepository.findTop31ByAppName(appName, new Sort(Sort.Direction.DESC, "time"));
                return getTimeSeriesFromQuery(queryResults);
            case "sixmonths":
                queryResults = clientAppSnapshotRepository.findTop186ByAppName(appName, new Sort(Sort.Direction.DESC, "time"));
                return getTimeSeriesFromQuery(queryResults);
        }
        return null;
    }

    // Needs wildly refactored
    private ClientAppTimeSeries getTimeSeriesFromQuery(List<ClientAppSnapshot> clientAppSnapshots) {
        Map<String, List<Object>> allTimeSeriesMetrics = new HashMap();

        for (ClientAppSnapshot clientAppSnapshot : clientAppSnapshots) {
            for(Map.Entry<String, Object> metric : clientAppSnapshot.getActuatorMetrics().entrySet()) {

                Long timeStamp = clientAppSnapshot.getTimeStamp();
                Object metricValue = metric.getValue();
                String metricName = metric.getKey();
                List<Object> timeSeries = Lists.newArrayList(timeStamp, metricValue);

                if (allTimeSeriesMetrics.containsKey(metricName)) {
                    allTimeSeriesMetrics.get(metricName).add(timeSeries);
                } else {
                    allTimeSeriesMetrics.put(metricName, new ArrayList<>());
                    allTimeSeriesMetrics.get(metricName).add(timeSeries);
                }
            }
        }
        return new ClientAppTimeSeries(allTimeSeriesMetrics);
    }
}

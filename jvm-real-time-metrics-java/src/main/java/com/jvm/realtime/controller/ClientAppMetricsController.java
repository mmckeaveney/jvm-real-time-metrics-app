package com.jvm.realtime.controller;

import com.google.common.collect.Lists;
import com.jvm.realtime.data.DockerProcessor;
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


    @RequestMapping(value = "/clientapps/names/all", method = RequestMethod.GET)
    public List<String> getAllClientApps() {
        List<String> clientNames = new ArrayList<>();
        List<ClientAppSnapshot> allClients = clientAppSnapshotRepository.findAll();
        for (ClientAppSnapshot app : allClients) {
            if (!clientNames.contains(app.getAppName())) {
                clientNames.add(app.getAppName());
            }
        }
        return clientNames;
    }

    @RequestMapping(value = "/timeseries", method = RequestMethod.GET)
    public ClientAppTimeSeries getTimeSeriesDataForSingleApp(@RequestParam(value = "appName") String appName,
                                                     @RequestParam(value = "timeScale") String timeScale) {
        List<ClientAppSnapshot> queryResults;
        switch (timeScale) {
            case "week":
                queryResults = clientAppSnapshotRepository.findTop7ByAppNameOrderByTimeStampDesc(appName);
                return getTimeSeriesFromQuery(queryResults);
            case "month":
                queryResults = clientAppSnapshotRepository.findTop31ByAppNameOrderByTimeStampDesc(appName);
                return getTimeSeriesFromQuery(queryResults);
            case "sixmonths":
                queryResults = clientAppSnapshotRepository.findTop186ByAppNameOrderByTimeStampDesc(appName);
                return getTimeSeriesFromQuery(queryResults);
        }
        return null;
    }

    /**
     * Gets the timeseries data for metrics required to draw a graph.
     * @param clientAppSnapshots the list of clientAppSnapshots to create the timeseries data with.
     * @return the ClientAppTimeSeries object used to draw a timeseries graph.
     */
    private ClientAppTimeSeries getTimeSeriesFromQuery(List<ClientAppSnapshot> clientAppSnapshots) {
        Map<String, List<Object>> allTimeSeriesMetrics = new HashMap();

        // For each application in the environment
        for (ClientAppSnapshot clientAppSnapshot : clientAppSnapshots) {
            // For each of the metrics in that application
            for(Map.Entry<String, Object> metric : clientAppSnapshot.getActuatorMetrics().entrySet()) {

                Long timeStamp = clientAppSnapshot.getTimeStamp();
                Object metricValue = metric.getValue();
                String metricName = metric.getKey();
                List<Object> timeSeries = Lists.newArrayList(timeStamp, metricValue);

                // Create a map of the timeseries metrics with the relevant appnames if it doesnt exist.
                if (allTimeSeriesMetrics.containsKey(metricName)) {
                    allTimeSeriesMetrics.get(metricName).add(timeSeries);
                } else {
                    // Simply add the timeseries metrics to the map because it already exists.
                    allTimeSeriesMetrics.put(metricName, new ArrayList<>());
                    allTimeSeriesMetrics.get(metricName).add(timeSeries);
                }
            }
        }
        // Return the timeseries data in one object back to the client.
        return new ClientAppTimeSeries(allTimeSeriesMetrics, clientAppSnapshots);
    }




}

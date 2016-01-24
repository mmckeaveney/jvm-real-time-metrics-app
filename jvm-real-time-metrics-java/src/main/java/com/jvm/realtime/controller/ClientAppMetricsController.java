package com.jvm.realtime.controller;

import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.persistence.ClientAppSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
public class ClientAppMetricsController {

    @Autowired
    ClientAppSnapshotRepository clientAppSnapshotRepository;

    @RequestMapping(value = "/timeseries", method = RequestMethod.GET)
    public List<ClientAppSnapshot> getTimeSeriesDataForSingleApp(@RequestParam(value = "appName") String appName,
                                                     @RequestParam(value = "timeScale") String timeScale) {
        List<ClientAppSnapshot> queryResults = new ArrayList<>();
        switch (timeScale) {
            case "week":
                queryResults = clientAppSnapshotRepository.findTop7ByAppName(appName, new Sort(Sort.Direction.DESC, "time"));
                break;
            case "month":
                queryResults = clientAppSnapshotRepository.findTop31ByAppName(appName, new Sort(Sort.Direction.DESC, "time"));
                break;
            case "sixmonths":
                queryResults = clientAppSnapshotRepository.findTop186ByAppName(appName, new Sort(Sort.Direction.DESC, "time"));
                break;
        }

        return queryResults;
    }
}

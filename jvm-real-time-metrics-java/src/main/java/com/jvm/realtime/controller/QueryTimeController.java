package com.jvm.realtime.controller;

import com.google.common.collect.Lists;
import com.jvm.realtime.data.QueryTimeProcessor;
import com.jvm.realtime.model.QueryTimeModel;
import com.jvm.realtime.persistence.QueryTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QueryTimeController {

    private QueryTimeProcessor queryTimeProcessor;
    private QueryTimeRepository queryTimeRepository;

    @Autowired
    public QueryTimeController(QueryTimeProcessor queryTimeProcessor, QueryTimeRepository queryTimeRepository) {
        this.queryTimeProcessor = queryTimeProcessor;
        this.queryTimeRepository = queryTimeRepository;
    }

    @RequestMapping(value = "/querytime/save", method = RequestMethod.POST)
    public QueryTimeModel handleQueryTimeFromClient(@RequestBody QueryTimeModel queryTimeModel) {
        queryTimeProcessor.notifyAndStoreQueryTime(queryTimeModel);
        return queryTimeModel;
    }
    @RequestMapping(value = "/api/querytime/mostRecent", method = RequestMethod.GET)
    public List<QueryTimeModel> getMostRecentQueryTime() {
        return Lists.newArrayList(queryTimeRepository.findTopByOrderByTimeExecuted());
    }

    @RequestMapping(value = "/api/querytime", method = RequestMethod.GET)
    public List<QueryTimeModel> getQueryTimesForApp(@RequestParam(value = "appName") String appName) {
        return queryTimeRepository.findByApplicationName(appName);
    }
}

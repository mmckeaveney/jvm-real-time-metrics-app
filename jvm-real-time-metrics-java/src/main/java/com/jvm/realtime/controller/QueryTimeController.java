package com.jvm.realtime.controller;

import com.jvm.realtime.data.QueryTimeProcessor;
import com.jvm.realtime.model.QueryTimeModel;
import com.jvm.realtime.persistence.QueryTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public QueryTimeModel handleExceptionFromClient(@RequestBody QueryTimeModel queryTimeModel) {
        queryTimeProcessor.notifyAndStoreQueryTime(queryTimeModel);
        return queryTimeModel;
    }
}

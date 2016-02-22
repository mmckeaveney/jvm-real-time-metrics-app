package com.jvm.realtime.controller;

import com.jvm.realtime.data.ExceptionProcessor;
import com.jvm.realtime.model.ExceptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class ExceptionController {

    @Autowired
    private ExceptionProcessor exceptionProcessor;

    @RequestMapping(value = "/exception", method = RequestMethod.POST)
    public void handleExceptionFromClient(@RequestBody ExceptionModel exceptionModel) {
        exceptionProcessor.notifyAndStoreException(exceptionModel);
    }


}

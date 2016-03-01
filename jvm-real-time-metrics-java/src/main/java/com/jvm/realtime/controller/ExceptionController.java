package com.jvm.realtime.controller;

import com.jvm.realtime.data.ExceptionProcessor;
import com.jvm.realtime.model.ExceptionModel;
import com.jvm.realtime.persistence.ExceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ExceptionController {

    private ExceptionProcessor exceptionProcessor;
    private ExceptionRepository exceptionRepository;

    @Autowired
    public ExceptionController(ExceptionProcessor exceptionProcessor, ExceptionRepository exceptionRepository) {
       this.exceptionProcessor = exceptionProcessor;
       this.exceptionRepository = exceptionRepository;
    }

    @RequestMapping(value = "/exception", method = RequestMethod.POST)
    public ExceptionModel handleExceptionFromClient(@RequestBody ExceptionModel exceptionModel) {
        exceptionProcessor.notifyAndStoreException(exceptionModel);
        return exceptionModel;
    }

    @RequestMapping(value = "/exception/all", method = RequestMethod.GET)
    public List<ExceptionModel> getAllExceptions() {
        return exceptionRepository.findAll();
    }

    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public List<ExceptionModel> getExceptionsForApp(@RequestParam(value = "appName") String appName) {
        return exceptionRepository.findByApplicationName(appName);
    }



}

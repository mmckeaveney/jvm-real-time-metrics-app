package com.jvm.realtime.controller;

import com.jvm.realtime.data.DockerProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class DockerController {

    @Autowired
    DockerProcessor dockerProcessor;

    @RequestMapping(value = "/docker/kill/{containerId}", method = RequestMethod.POST)
    public void killDockerContainer(@PathVariable("containerId") String containerId) {
        dockerProcessor.killApp(containerId);
    }

    @RequestMapping(value = "/docker/restart/{containerId}", method = RequestMethod.POST)
    public void restartDockerContainer(@PathVariable("containerId") String containerId) {
        dockerProcessor.restartApp(containerId);
    }
}

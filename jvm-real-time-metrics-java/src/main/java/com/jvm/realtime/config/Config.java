package com.jvm.realtime.config;

import com.jvm.realtime.rest.RealTimeDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Autowired
    private RealTimeDataClient realTimeDataClient;

    public static final String dockerHost = "localhost";
    public static final String dockerCertsPath = "/Users/martinmckeaveney/.docker/machine/certs";



}

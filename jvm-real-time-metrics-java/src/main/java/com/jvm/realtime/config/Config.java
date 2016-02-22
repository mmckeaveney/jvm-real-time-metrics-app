package com.jvm.realtime.config;

import com.jvm.realtime.data.RealTimeDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Filter;

@Configuration
public class Config {

    public static final String dockerHost = "localhost";
    public static final String dockerCertsPath = "/Users/martinmckeaveney/.docker/machine/certs";

    @Autowired
    private RealTimeDataClient realTimeDataClient;

//    @Bean
//    public Filter authFilter() {
//        return new OAuthWebFilter();
//    }



}

package com.jvm.realtime.config;

import com.jvm.realtime.client.JvmrtExceptionHandler;
import com.jvm.realtime.data.RealTimeDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;


@Configuration
@ComponentScan (
    excludeFilters = @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value={JvmrtExceptionHandler.class})
)
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

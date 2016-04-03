package com.jvm.realtime.config;

import com.jvm.realtime.client.IgnoreDuringScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@ComponentScan(basePackages ="com.jvm.realtime",
        excludeFilters = @ComponentScan.Filter(IgnoreDuringScan.class))
@Configuration
public class Config {


}

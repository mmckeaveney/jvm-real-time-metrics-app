package com.jvm.realtime.config;

import com.jvm.realtime.client.IgnoreDuringScan;
import com.jvm.realtime.client.SQLProfiler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@ComponentScan(basePackages ="com.jvm.realtime.*", excludeFilters = @ComponentScan.Filter(value = IgnoreDuringScan.class))
@Configuration
public class Config {

}

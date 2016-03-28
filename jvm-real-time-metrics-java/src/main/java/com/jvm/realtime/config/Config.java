package com.jvm.realtime.config;

import com.jvm.realtime.client.JvmrtExceptionHandler;
import com.jvm.realtime.data.RealTimeDataClient;
import com.jvm.realtime.email.EmailService;
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

    @Autowired
    private RealTimeDataClient realTimeDataClient;
    @Autowired
    private EmailService emailService;

//    @Bean
//    public Filter authFilter() {
//        return new OAuthWebFilter();
//    }

    @Bean
    public String sendEmailTest() {
       emailService.sendEmail();
        return "Email sent";
    }



}

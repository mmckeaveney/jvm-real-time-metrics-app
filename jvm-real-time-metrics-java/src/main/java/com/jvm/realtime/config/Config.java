package com.jvm.realtime.config;

import com.jvm.realtime.data.RealTimeDataClient;
import com.jvm.realtime.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


//@ComponentScan(basePackages ="com.jvm.realtime",
//        excludeFilters = @ComponentScan.Filter(IgnoreDuringScan.class))
@Configuration
public class Config {

    @Autowired
    private RealTimeDataClient realTimeDataClient;
    @Autowired
    private EmailService emailService;

//    @Bean
//    public Filter authFilter() {
//        return new OAuthWebFilter();
//    }

//    @Bean
//    public String sendEmailTest() {
//       emailService.sendEmail();
//        return "Email sent";
//    }



}

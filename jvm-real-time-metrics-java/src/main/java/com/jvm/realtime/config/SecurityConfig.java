package com.jvm.realtime.config;

import com.auth0.spring.security.auth0.Auth0AuthenticationEntryPoint;
import com.auth0.spring.security.auth0.Auth0AuthenticationFilter;
import com.auth0.spring.security.auth0.Auth0AuthenticationProvider;
import org.apache.catalina.filters.CorsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;

@ComponentScan("com.auth0")
@PropertySource("classpath:auth0.properties")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Value(value = "${auth0.clientId}")
    private String clientId;

    @Value(value = "${auth0.clientSecret}")
    private String clientSecret;

    @Value(value = "${auth0.domain}")
    private String issuer;

    @Value(value = "${auth0.securedRoute}")
    private String securedRoute;

    @Bean
    Filter simpleCORSFilter() {
        return new SimpleCORSFilter();
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean(name = "auth0AuthenticationProvider")
    public Auth0AuthenticationProvider auth0AuthenticationProvider(){

        Auth0AuthenticationProvider authenticationProvider = new Auth0AuthenticationProvider();
        authenticationProvider.setClientId(clientId);
        authenticationProvider.setClientSecret(clientSecret);
        authenticationProvider.setSecuredRoute(securedRoute);

        return authenticationProvider;
    }

    @Bean(name = "auth0EntryPoint")
    public Auth0AuthenticationEntryPoint auth0AuthenticationEntryPoint() {
        return new Auth0AuthenticationEntryPoint();
    }

    @Bean(name = "auth0Filter")
    public Auth0AuthenticationFilter auth0AuthenticationFilter(Auth0AuthenticationEntryPoint entryPoint) {
        Auth0AuthenticationFilter filter = new Auth0AuthenticationFilter();
        filter.setEntryPoint(entryPoint);
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(auth0AuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(auth0AuthenticationFilter(auth0AuthenticationEntryPoint()), SecurityContextPersistenceFilter.class)
                // Filter the requests to the server. Check for JSON Web Token and put through CORS Filter.
                .addFilterBefore(simpleCORSFilter(), Auth0AuthenticationFilter.class)
                .authorizeRequests()
                // Any requests to /api
                .antMatchers(securedRoute)
                // Make sure they are authenticated
                .authenticated();
    }
}


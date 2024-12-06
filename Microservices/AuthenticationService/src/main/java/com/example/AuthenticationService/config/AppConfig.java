package com.example.AuthenticationService.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced  // Enable load balancing and service discovery with Eureka
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

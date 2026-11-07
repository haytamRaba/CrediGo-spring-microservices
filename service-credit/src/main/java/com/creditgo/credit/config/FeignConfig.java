package com.creditgo.credit.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.creditgo.credit.client")
public class FeignConfig {
}

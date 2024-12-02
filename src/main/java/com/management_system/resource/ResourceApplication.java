package com.management_system.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
        exclude = {SecurityAutoConfiguration.class},
        scanBasePackages = {
                "com.management_system.resource.usecases",
                "com.management_system.resource.entities",
                "com.management_system.resource.infrastructure",
                "com.management_system.resource.config",
                "com.management_system.resource",
                "com.management_system.utilities",
        }
)
@ComponentScan(basePackages = {
        "com.management_system.resource.usecases",
        "com.management_system.resource.entities",
        "com.management_system.resource.infrastructure",
        "com.management_system.resource.config",
        "com.management_system.resource",
        "com.management_system.utilities",
})
//@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
public class ResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(com.management_system.resource.ResourceApplication.class, args);
    }
}

package com.xyz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@SpringBootApplication
//@EnableDiscoveryClient
@EnableSwagger2
public class ServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(ServiceApplication.class, args);
        }
}

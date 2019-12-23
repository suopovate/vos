package com.vt.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author vate
 * @date 2019/12/18 23:03
 */
@SpringBootApplication
@EnableEurekaClient
public class RestApiApp {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(RestApiApp.class);
        springApplication.run(args);
    }
}

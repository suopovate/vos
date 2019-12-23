package com.vt.eurake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author vate
 * @date 2019/12/18 23:03
 */
@SpringBootApplication
@EnableEurekaServer
public class EurakeApp {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(EurakeApp.class);
        springApplication.run(args);
    }
}

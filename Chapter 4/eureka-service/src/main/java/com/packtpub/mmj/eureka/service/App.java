package com.packtpub.mmj.eureka.service;

/**
 *
 * @author Sourabh Sharma
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 *
 * @author Sourabh Sharma
 */
@SpringBootApplication
@EnableEurekaServer
public class App {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

package com.packtpub.mmj.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *
 * @author Sourabh Sharma
 */
@SpringBootApplication
@EnableEurekaClient
public class RestaurantApp {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RestaurantApp.class, args);
    }
}

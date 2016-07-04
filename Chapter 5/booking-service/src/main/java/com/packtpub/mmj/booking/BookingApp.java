package com.packtpub.mmj.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *
 * @author Sourabh Sharma
 */
@SpringBootApplication
@EnableEurekaClient
public class BookingApp {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(BookingApp.class, args);
    }

}

package com.msa.demo.sockshop.orders;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableServiceComb
@EnableAsync
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}

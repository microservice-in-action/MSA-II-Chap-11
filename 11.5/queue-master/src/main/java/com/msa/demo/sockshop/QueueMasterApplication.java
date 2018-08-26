package com.msa.demo.sockshop;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class QueueMasterApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(QueueMasterApplication.class, args);
    }
}

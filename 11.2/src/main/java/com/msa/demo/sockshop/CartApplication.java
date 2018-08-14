package com.msa.demo.sockshop;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
@EnableServiceComb
public class CartApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(CartApplication.class, args);
	}
}
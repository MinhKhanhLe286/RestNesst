package com.pbl5cnpm.airbnb_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AirbnbServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirbnbServiceApplication.class, args);
		System.out.println("Server runing!");
	}

}

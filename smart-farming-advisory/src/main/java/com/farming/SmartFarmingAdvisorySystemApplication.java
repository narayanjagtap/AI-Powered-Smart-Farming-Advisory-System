package com.farming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.farming")
public class SmartFarmingAdvisorySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartFarmingAdvisorySystemApplication.class, args);
	}
}
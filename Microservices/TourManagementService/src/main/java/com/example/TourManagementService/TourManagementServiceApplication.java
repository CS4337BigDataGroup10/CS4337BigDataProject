package com.example.TourManagementService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TourManagementServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TourManagementServiceApplication.class, args);
	}
}
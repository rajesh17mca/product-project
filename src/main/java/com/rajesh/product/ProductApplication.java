package com.rajesh.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class ProductApplication {
	private static final Logger logger = LogManager.getLogger(ProductApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Spring Boot application...");
		SpringApplication.run(ProductApplication.class, args);
	}

}

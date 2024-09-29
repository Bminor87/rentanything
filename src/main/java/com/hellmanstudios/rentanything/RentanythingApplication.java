package com.hellmanstudios.rentanything;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RentanythingApplication {

	private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RentanythingApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			// TODO: Add some demo data
		};
	}

}

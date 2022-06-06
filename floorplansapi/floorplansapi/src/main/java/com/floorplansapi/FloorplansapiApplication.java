package com.floorplansapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FloorplansapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FloorplansapiApplication.class, args);
	}

}

package com.techstack.learn.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LearnAndApplySpringBootActuatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnAndApplySpringBootActuatorApplication.class, args);
	}

}

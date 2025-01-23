package com.junggotown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
public class JunggoTownApplication {

	public static void main(String[] args) {
		SpringApplication.run(JunggoTownApplication.class, args);
	}

}

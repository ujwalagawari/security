package com.cg.centralizedconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class CentralizedConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralizedConfigApplication.class, args);
	}

}


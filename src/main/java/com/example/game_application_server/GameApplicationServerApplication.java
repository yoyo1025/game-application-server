package com.example.game_application_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class GameApplicationServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GameApplicationServerApplication.class, args);
	}
}

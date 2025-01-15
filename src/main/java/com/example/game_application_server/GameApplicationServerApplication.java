package com.example.game_application_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.game_application_server.infrastructure")
public class GameApplicationServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GameApplicationServerApplication.class, args);
	}
}

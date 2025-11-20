package com.pedrosanchez.netflix_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// Clase principal que inicia la aplicación Spring Boot.
@SpringBootApplication
@EnableScheduling
public class NetflixCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixCloneApplication.class, args);
	}

}

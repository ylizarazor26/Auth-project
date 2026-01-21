package com.auth_service.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {

		final Logger log;
		log = LogManager.getLogger(ProjectApplication.class);
		SpringApplication.run(ProjectApplication.class, args);
		log.info("Inicio correctamente la aplicacion");
		log.info(new BCryptPasswordEncoder().encode("Password123"));
	}

}

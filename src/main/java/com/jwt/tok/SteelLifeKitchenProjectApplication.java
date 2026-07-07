package com.jwt.tok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.jwt.tok")
public class SteelLifeKitchenProjectApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SteelLifeKitchenProjectApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SteelLifeKitchenProjectApplication.class, args);
	}
}

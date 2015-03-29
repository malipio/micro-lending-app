package com.github.malipio.micro.lending.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.AsyncRestTemplate;

@SpringBootApplication
public class MicroLendingApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MicroLendingApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MicroLendingApplication.class, args);
	}
	
	

}

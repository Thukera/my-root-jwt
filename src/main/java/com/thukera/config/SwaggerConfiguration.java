package com.thukera.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"Spring Boot Simples REST API",
				"Spring Boot Simple REST API Swagger Documentation",
				"Version 1",
				"urn:tos",
				new Contact("Admin", "www.a5solutions.com", "aneto@a5solutions.com"),
				"Apache 2.0",
				"https://www.apache.org/licenses/LICENSE-2.0",
				new ArrayList<>()
				);
	}
	
}

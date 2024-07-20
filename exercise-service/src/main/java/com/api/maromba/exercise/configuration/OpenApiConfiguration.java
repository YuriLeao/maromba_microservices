package com.api.maromba.exercise.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;

@Configuration
@OpenAPIDefinition(info = 
	@Info(
		title = "Exercise Service API",
		version = "V1",
		contact = 
			@Contact(
				name = "Yuri", email = "ydomingosleao@gmail.com"
			),
		license =
			@io.swagger.v3.oas.annotations.info.License(
				name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
			),
		description = "Documentation of the Exercise Service API"
	),
	servers =
		@Server(
			url = "${api.server.url}",
			description = "${api.server.description}"
		)
)
@SecurityScheme(
		  name = "Bearer Authentication",
		  type = SecuritySchemeType.HTTP,
		  bearerFormat = "JWT",
		  scheme = "bearer"
)
public class OpenApiConfiguration {

    @Bean
    OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components())
				.info(new io.swagger.v3.oas.models.info.Info()
						.title("Exercise Service API")
						.version("v1")
						.license(new License()
								.name("Apache 2.0")
								.url("http://springdoc.org")
						)
				);
	}
}

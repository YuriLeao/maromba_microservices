package com.api.maromba.gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.maromba.gateway.filter.JwtAuthenticationFilter;

@Configuration
public class GatewayConfiguration {

	@Autowired
	private JwtAuthenticationFilter filter;

    @Bean
    RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("usuario-service",
                        r -> r.path("/usuario-service/**").filters(f -> f.filter(filter)).uri("lb://usuario-service"))
                .route("empresa-service",
                        r -> r.path("/empresa-service/**").filters(f -> f.filter(filter)).uri("lb://empresa-service"))
                .route("exercicio-service",
                        r -> r.path("/exercicio-service/**").filters(f -> f.filter(filter)).uri("lb://exercicio-service"))
                .route("grupo-muscular-service",
                        r -> r.path("/grupo-muscular-service/**").filters(f -> f.filter(filter)).uri("lb://grupo-muscular-service"))
                .route("movimento-service",
                        r -> r.path("/movimento-service/**").filters(f -> f.filter(filter)).uri("lb://movimento-service"))
                .route("treino-service",
                        r -> r.path("/treino-service/**").filters(f -> f.filter(filter)).uri("lb://treino-service"))
                
                .build();
    }

}

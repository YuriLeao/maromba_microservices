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
                .route("user-service",
                        r -> r.path("/user-service/**").filters(f -> f.filter(filter)).uri("lb://user-service"))
                .route("company-service",
                        r -> r.path("/company-service/**").filters(f -> f.filter(filter)).uri("lb://company-service"))
                .route("exercise-service",
                        r -> r.path("/exercise-service/**").filters(f -> f.filter(filter)).uri("lb://exercise-service"))
                .route("workout-sheet-service",
                        r -> r.path("/workout-sheet-service/**").filters(f -> f.filter(filter)).uri("lb://workout-sheet-service"))
                .route("workout-service",
                        r -> r.path("/workout-service/**").filters(f -> f.filter(filter)).uri("lb://workout-service"))                
                .build();
    }

}

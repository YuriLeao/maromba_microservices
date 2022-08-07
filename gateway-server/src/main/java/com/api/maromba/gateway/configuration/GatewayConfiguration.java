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
                .route("empresa-service",
                        r -> r.path("/empresa-service/**").filters(f -> f.filter(filter)).uri("lb://empresa-service"))
                .route("usuario-service",
                        r -> r.path("/usuario-service/**").filters(f -> f.filter(filter)).uri("lb://usuario-service"))
                .build();
    }

}

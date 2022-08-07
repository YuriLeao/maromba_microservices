package com.api.maromba.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.api.maromba.gateway.util.JwtUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

		final List<String> apiEndpoints = List.of("/login", "/usuario-service/v3/api-docs",
				"/empresa-service/v3/api-docs");

		Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
				.noneMatch(uri -> r.getURI().getPath().contains(uri));

		if (isApiSecured.test(request)) {
			List<String> authorization = request.getHeaders().getOrEmpty("Authorization");
			if (authorization.size() <= 0 || authorization.get(0).isEmpty()
					|| !authorization.get(0).startsWith("Bearer ")) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				response.getHeaders().add("error", "No authorization was recorded.");
				String erro = "{\n	\"error_menssage\": \"No authorization was recorded.\"\n}";
				byte[] bytes = erro.getBytes(StandardCharsets.UTF_8);
				DataBuffer buffer = response.bufferFactory().wrap(bytes);
				return response.writeWith(Flux.just(buffer));
			}

			final String token = authorization.get(0).substring("Bearer ".length());

			try {
				jwtUtil.validateToken(token);
			} catch (Exception exception) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.FORBIDDEN);
				response.getHeaders().add("error", exception.getMessage());
				String erro = "{\n	\"error_menssage\": \"".concat(exception.getMessage()).concat("\"\n}");
				byte[] bytes = erro.getBytes(StandardCharsets.UTF_8);
				DataBuffer buffer = response.bufferFactory().wrap(bytes);
				return response.writeWith(Flux.just(buffer));
			}
		}

		return chain.filter(exchange);
	}

}

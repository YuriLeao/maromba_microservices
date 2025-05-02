package com.api.maromba.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.api.maromba.gateway.exception.RoleException;
import com.api.maromba.gateway.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class JwtAuthenticationFilter implements GlobalFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

		final List<String> apiEndpoints = List.of("/login", "/v3/api-docs", "/user-service/v3/api-docs", "/gender-service/v3/api-docs",
				"/authorization-service/v3/api-docs", "/company-service/v3/api-docs", "/exercise-service/v3/api-docs",
				"/muscleGroup-service/v3/api-docs", "/workout-sheet-service/v3/api-docs",
				"/workout-service/v3/api-docs");

		Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
				.noneMatch(uri -> r.getURI().getPath().contains(uri));

		if (isApiSecured.test(request)) {
			List<String> authorization = request.getHeaders().getOrEmpty("Authorization");
			if (authorization.size() <= 0 || authorization.get(0).isEmpty()
					|| !authorization.get(0).startsWith("Bearer ")) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				response.getHeaders().add("error", "No authorization was recorded.");
				String erro = "{\n	\"error_message\": \"No authorization was recorded.\"\n}";
				byte[] bytes = erro.getBytes(StandardCharsets.UTF_8);
				DataBuffer buffer = response.bufferFactory().wrap(bytes);
				return response.writeWith(Flux.just(buffer));
			}

			final String token = authorization.get(0).substring("Bearer ".length());

			try {
				DecodedJWT decodedJWT = jwtUtil.validateToken(token, "/user-service/login");

				roleFilter(request, decodedJWT);

			} catch (RoleException exception) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.FORBIDDEN);
				response.getHeaders().add("error", exception.getDescription());
				String erro = "{\n	\"error_message\": \"".concat(exception.getDescription()).concat("\"\n}");
				byte[] bytes = erro.getBytes(StandardCharsets.UTF_8);
				DataBuffer buffer = response.bufferFactory().wrap(bytes);
				return response.writeWith(Flux.just(buffer));
			} catch (Exception exception) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.FORBIDDEN);
				response.getHeaders().add("error", exception.getMessage());
				String erro = "{\n	\"error_message\": \"".concat(exception.getMessage()).concat("\"\n}");
				byte[] bytes = erro.getBytes(StandardCharsets.UTF_8);
				DataBuffer buffer = response.bufferFactory().wrap(bytes);
				return response.writeWith(Flux.just(buffer));
			}
		}

		return chain.filter(exchange);
	}

	private void roleFilter(ServerHttpRequest request, DecodedJWT decodedJWT) throws RoleException {
		if (!(request.getURI().getPath().contains("/user-service/")
				|| request.getURI().getPath().contains("/gender-service/")
				|| request.getURI().getPath().contains("/authorization-service/")
				|| request.getURI().getPath().contains("/company-service/update")
				|| request.getURI().getPath().contains("/company-service/getById")
				|| request.getURI().getPath().contains("/exercise-service/")
				|| request.getURI().getPath().contains("/muscleGroup-service/")
				|| request.getURI().getPath().contains("/workout-service/")
				|| request.getURI().getPath().contains("/workout-sheet-service/"))
				&& decodedJWT.getClaim("authorization").asString().contains("E")) {
			throw new RoleException("Service don't authorizate for this user.");
		} else if (!(request.getURI().getPath().contains("/user-service/")
				|| request.getURI().getPath().contains("/gender-service/")
				|| request.getURI().getPath().contains("/authorization-service/")
				|| request.getURI().getPath().contains("/company-service/getById")
				|| request.getURI().getPath().contains("/exercise-service/")
				|| request.getURI().getPath().contains("/muscleGroup-service/")
				|| request.getURI().getPath().contains("/workout-sheet-service/")
				|| request.getURI().getPath().contains("/workout-service/"))
				&& decodedJWT.getClaim("authorization").asString().contains("P")) {
			throw new RoleException("Service don't authorizate for this user.");
		} else if (!(request.getURI().getPath().contains("/user-service/update")
				|| request.getURI().getPath().contains("/user-service/login")
				|| request.getURI().getPath().contains("/gender-service/")
				|| request.getURI().getPath().contains("/company-service/getById")
				|| request.getURI().getPath().contains("/exercise-service/getById")
				|| request.getURI().getPath().contains("/exercise-service/getAll")
				|| request.getURI().getPath().contains("/muscleGroup-service/")
				|| request.getURI().getPath().contains("/workout-sheet-service/getById")
				|| request.getURI().getPath().contains("/workout-sheet-service/getAll")
				|| request.getURI().getPath().contains("/workout-service/getById")
				|| request.getURI().getPath().contains("/workout-service/getAll"))
				&& decodedJWT.getClaim("authorization").asString().contains("AL")) {
			throw new RoleException("Service don't authorizate for this user.");
		} else if (!decodedJWT.getClaim("authorization").asString().contains("A")) {
			throw new RoleException("Service don't authorizate for this user.");
		}
	}

}

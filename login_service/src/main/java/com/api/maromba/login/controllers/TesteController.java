package com.api.maromba.login.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("usuario")
public class TesteController {
	
	private Logger logger = LoggerFactory.getLogger(TesteController.class);
	
	@GetMapping("/teste")
	//@Retry(name = "default")
	//@CircuitBreaker(name = "default", fallbackMethod = "fallBackMethod")
	//@RateLimiter(name = "default")
	@Bulkhead(name = "default")
	public String teste() {
		logger.info("Request para teste recebida");
		var response = new RestTemplate().getForEntity("htpp://localhost:8080/teste", String.class);
		return response.getBody();
	}
	
	public String fallBackMethod(Exception ex) {
		return "metodo de erro";
	}

}

package com.api.maromba.gateway.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {

	private Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
	@Value("${jwt.secret}")
	private String jwtSecret;

	public void validateToken(final String token) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes()); 
		    JWTVerifier verifier = JWT.require(algorithm)
		        .build();
		    verifier.verify(token);
		} catch (Exception exception){
		    logger.error("Erro ao validar token: ", exception.getMessage());
		    throw exception;
		}
	}

}
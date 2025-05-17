package com.api.maromba.exercise.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {
	
	private final String SUBJECT = "teste";
	
	private final String CLAIM = "A";

	private Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	public String generateTokenTeste(String issuer) {
		Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
		String token = JWT.create()
				.withSubject(SUBJECT)
				.withIssuer(issuer)
				.withClaim("authorization", CLAIM)
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
				.sign(algorithm);
		return token;
	}
	

	public DecodedJWT validateToken(final String token, String issuer) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
		    JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer)
		        .build();
		    return verifier.verify(token);
		} catch (Exception exception){
		    logger.error("Error validating token: ", exception.getMessage());
		    throw exception;
		}
	}

}
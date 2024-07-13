package com.api.maromba.user.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.api.maromba.user.models.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {

	private Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	public String generateToken(UserModel user, String issuer) {
		Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
		String token = JWT.create()
				.withSubject(user.getId().toString())
				.withIssuer(issuer)
				.withClaim("email", user.getEmail())
				.withClaim("authorizations", user.getAuthorizations())
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
				.sign(algorithm);
		return token;
	}

	public void validateToken(final String token, String issuer) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
		    JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer)
		        .build();
		    verifier.verify(token);
		} catch (Exception exception){
		    logger.error("Error validating token: ", exception.getMessage());
		    throw exception;
		}
	}

}
package com.api.maromba.usuario.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.api.maromba.usuario.models.UsuarioModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {

	private Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	public String generateToken(UsuarioModel usuario, String issuer) {
		Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
		String token = JWT.create()
				.withSubject(usuario.getId().toString())
				.withIssuer(issuer)
				.withClaim("email", usuario.getEmail())
				.withClaim("autorizacoes", usuario.getAutorizacoes())
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
		    logger.error("Erro ao validar token: ", exception.getMessage());
		    throw exception;
		}
	}

}
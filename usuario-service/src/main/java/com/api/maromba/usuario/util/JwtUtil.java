package com.api.maromba.usuario.util;

import java.time.format.DateTimeFormatter;
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
	
	public String generateToken(UsuarioModel usuario) {
		Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
		String token = JWT.create()
				.withSubject(usuario.getId().toString())
				.withSubject(usuario.getUsuario())
				.withSubject(usuario.getEmail())
				.withSubject(usuario.getGenero())
				.withSubject(usuario.getTelefone())
				.withSubject(usuario.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.withSubject(usuario.getEmpresaId().toString())
				.withSubject(usuario.getPeso().toString())
				.withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
				.withIssuer("Autorizacao")
				.sign(algorithm);
		return token;
	}

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
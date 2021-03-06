package com.api.maromba.usuario.proxy;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "empresa-service")
public interface EmpresaProxy {
	
	@GetMapping("/empresa-service/obterById/{id}")
	public ResponseEntity<Object> obterById(@PathVariable(value = "id") UUID id);
		
}

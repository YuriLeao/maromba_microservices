package com.api.maromba.user.proxy;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "company-service")
public interface CompanyProxy {
	
	@GetMapping("/company-service/getById/{id}")
	public ResponseEntity<Object> getById(@PathVariable(value = "id") UUID id);
		
}

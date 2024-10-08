package com.api.maromba.user.proxy;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.api.maromba.user.dtos.CompanyDTO;


@FeignClient(name = "company-service")
public interface CompanyProxy {
	
	@GetMapping("company-service/getById/{id}")
	public ResponseEntity<CompanyDTO> getById(@PathVariable(value = "id") UUID id);
		
}

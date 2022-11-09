package com.api.maromba.gateway;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayServerApplication {

	public static void main(String[] args) throws InterruptedException {
		TimeUnit.SECONDS.sleep(180);
		SpringApplication.run(GatewayServerApplication.class, args);
	}

}

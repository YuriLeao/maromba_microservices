package com.api.maromba.empresa.comfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties("loginservice")
public class EmpresaConfiguration {
	private String ambiente;

	public EmpresaConfiguration() {
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

}

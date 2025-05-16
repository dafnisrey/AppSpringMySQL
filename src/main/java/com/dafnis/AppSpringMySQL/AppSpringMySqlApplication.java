package com.dafnis.AppSpringMySQL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@SpringBootApplication
public class AppSpringMySqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppSpringMySqlApplication.class, args);
	}

}

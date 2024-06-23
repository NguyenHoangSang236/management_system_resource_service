package com.management_system.ingredient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
		exclude = {SecurityAutoConfiguration.class},
		scanBasePackages = {
				"com.management_system.ingredient.usecases",
				"com.management_system.ingredient.entities",
				"com.management_system.ingredient.infrastructure",
				"com.management_system.ingredient",
				"com.management_system.utilities",
		}
)
@ComponentScan(basePackages = {
		"com.management_system.ingredient.usecases",
		"com.management_system.ingredient.entities",
		"com.management_system.ingredient.infrastructure",
		"com.management_system.ingredient",
		"com.management_system.utilities",
})
@EnableDiscoveryClient
public class IngredientApplication {

	public static void main(String[] args) {
		SpringApplication.run(IngredientApplication.class, args);
	}

}

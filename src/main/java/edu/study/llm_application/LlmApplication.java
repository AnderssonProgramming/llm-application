package edu.study.llm_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "edu.study.llm_application")
public class LlmApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmApplication.class, args);
	}

}

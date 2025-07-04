package com.smarthire.resume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = { "com.smarthire.resume", "com.smarthirepro" })
@EntityScan(basePackages = { "com.smarthire.resume.domain.model", "com.smarthirepro.domain.model" })
@EnableJpaRepositories(basePackages = { "com.smarthire.resume.domain.repository",
		"com.smarthirepro.domain.repositories" })
public class ResumeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeApplication.class, args);
	}

}

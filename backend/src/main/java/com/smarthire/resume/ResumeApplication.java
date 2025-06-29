package com.smarthire.resume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = {
		"com.smarthire.resume",
		"com.smarthirepro.core"
})
@EntityScan(basePackages = { "com.smarthirepro.domain.model", "com.smarthire.resume.domain.model" })
public class ResumeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeApplication.class, args);
	}

}

package com.findmecore.findmecore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = "com.findmecore")
@EnableJpaRepositories
@EnableTransactionManagement
public class FindMeCoreApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(FindMeCoreApplication.class, args);

	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FindMeCoreApplication.class);
	}

}

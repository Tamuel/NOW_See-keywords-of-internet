package com.softart;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@ComponentScan("com.softart.")
@EnableJpaRepositories(basePackages = { "com.softart.repository" })
public class ApplicationConfig {


}


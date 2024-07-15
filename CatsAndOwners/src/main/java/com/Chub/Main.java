package com.Chub;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.Chub"})
@EnableJpaRepositories(basePackages = "com.Chub.dao.repositories")
@EntityScan(basePackages = "com.Chub.dao.Models.*")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
package com.alonso.event.store.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.alonso.event.store")
@EnableJpaRepositories("com.alonso.event.store.jpa")
@EntityScan("com.alonso.event.store.jpa")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

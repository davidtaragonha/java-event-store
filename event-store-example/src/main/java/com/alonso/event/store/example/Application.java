package com.alonso.event.store.example;

import com.alonso.event.store.jpa.EnableEventStoreJpa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEventStoreJpa
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

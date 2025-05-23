package ru.practicum.main.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"ru.practicum.main.service", "client"})
public class MainServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }
}

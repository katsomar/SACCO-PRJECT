package com.example.sacco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SaccoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaccoApplication.class, args);
    }
}

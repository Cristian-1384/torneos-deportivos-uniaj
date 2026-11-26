package com.example.torneosdeportivos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TorneosDeportivosApplication {
    public static void main(String[] args) {
        SpringApplication.run(TorneosDeportivosApplication.class, args);
    }
}
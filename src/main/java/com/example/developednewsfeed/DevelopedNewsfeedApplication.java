package com.example.developednewsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevelopedNewsfeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevelopedNewsfeedApplication.class, args);
    }

}

package com.example.developednewsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevelopedNewsfeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevelopedNewsfeedApplication.class, args);
    }

}

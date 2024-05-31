package com.example.tasklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TasklistApplication {
    public static void main(final String[] args) {
        SpringApplication.run(TasklistApplication.class, args);
    }
}

package com.alconn.copang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CopangApplication {

    public static void main(String[] args) {
        SpringApplication.run(CopangApplication.class, args);
    }

}

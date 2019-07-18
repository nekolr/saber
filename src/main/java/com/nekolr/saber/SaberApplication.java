package com.nekolr.saber;

import org.hashids.Hashids;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class SaberApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaberApplication.class, args);
    }

    @Bean
    public Hashids hashids() {
        return new Hashids(SaberApplication.class.toString());
    }
}

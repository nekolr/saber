package com.nekolr.saber;

import org.hashids.Hashids;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableCaching
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class SaberApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaberApplication.class, args);
    }

    @Bean
    public Hashids hashids() {
        return new Hashids(SaberApplication.class.toString());
    }
}

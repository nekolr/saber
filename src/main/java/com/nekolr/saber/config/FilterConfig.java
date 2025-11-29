package com.nekolr.saber.config;

import com.nekolr.saber.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public FilterRegistrationBean<@NonNull JwtAuthenticationFilter> jwtAuthenticationFilterRegistration() {
        FilterRegistrationBean<@NonNull JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        registrationBean.setName("jwtAuthenticationFilter");
        return registrationBean;
    }
}
package com.nekolr.saber.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;

/**
 * @author nekolr
 */
@Configuration
public class CacheKeyConfig {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append("-");
            sb.append(method.getName());
            sb.append("-");
            for (Object obj : params) {
                if (obj instanceof PageRequest) {
                    sb.append(obj.hashCode());
                } else {
                    try {
                        sb.append(mapper.writeValueAsString(obj).hashCode());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        };
    }

}

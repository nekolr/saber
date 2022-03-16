package com.nekolr.saber.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "filesystem")
public class WebResourceConfig implements WebMvcConfigurer {

    @Value("${storage.filesystem.imgFolder}")
    private String imgFolder;
    @Value("${storage.filesystem.cacheTime}")
    private Duration cacheTime;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源映射
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        // 图床目录映射
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + imgFolder.replace("\\", "/"))
                .setCacheControl(CacheControl.maxAge(cacheTime.get(ChronoUnit.SECONDS), TimeUnit.SECONDS));
    }
}
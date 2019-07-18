package com.nekolr.saber.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class PropertyConfig {

    /**
     * 这样就可以通过 @Value(${xxx.xxx.xxx}) 的方式获取配置项
     *
     * @return
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        // 处理读取配置文件中文会出现乱码的问题
        configurer.setFileEncoding("UTF-8");
        return configurer;
    }
}

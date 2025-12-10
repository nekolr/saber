package com.nekolr.saber.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

/**
 * 国际化配置
 */
@Configuration
public class I18nConfig {

    /**
     * 配置 LocaleResolver
     * 使用 AcceptHeaderLocaleResolver 根据 HTTP 请求头的 Accept-Language 来确定语言
     */
    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        // 设置默认语言为简体中文
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        // 设置支持的语言列表
        resolver.setSupportedLocales(Arrays.asList(
                Locale.SIMPLIFIED_CHINESE,  // zh_CN
                Locale.ENGLISH            // en
        ));
        return resolver;
    }
}
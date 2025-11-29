package com.nekolr.saber.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String type;
    private Filesystem filesystem;

    @Getter
    @Setter
    public static class Filesystem {
        private String imgFolder;
        private Duration cacheTime;
    }
}
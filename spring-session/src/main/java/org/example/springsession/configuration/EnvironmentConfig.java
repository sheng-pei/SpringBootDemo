package org.example.springsession.configuration;

import org.example.springsession.keys.ConfigKeyCenter;
import org.example.springsession.keys.KeyCenter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentConfig {
    @Bean
    @ConfigurationProperties(prefix = "key-center")
    public KeyCenter keyCenter() {
        return new ConfigKeyCenter();
    }

}

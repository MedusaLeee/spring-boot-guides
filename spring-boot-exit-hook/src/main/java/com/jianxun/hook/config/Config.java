package com.jianxun.hook.config;

import com.jianxun.hook.component.ReleaseManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class Config {
    @Bean
    @Scope("singleton")
    public ReleaseManager releaseManager() {
        return new ReleaseManager();
    }
}

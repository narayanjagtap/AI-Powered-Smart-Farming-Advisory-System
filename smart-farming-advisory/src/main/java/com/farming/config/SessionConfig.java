package com.farming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Collections;

@Configuration
public class SessionConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // PUBLIC REPO CONFIG: Generalized settings for build/demo purposes
        config.setAllowCredentials(true);

        // CHANGED: Removed specific URLs (like your GitHub Page) for privacy.
        // Using a wildcard pattern allows the code to run anywhere without errors.
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}

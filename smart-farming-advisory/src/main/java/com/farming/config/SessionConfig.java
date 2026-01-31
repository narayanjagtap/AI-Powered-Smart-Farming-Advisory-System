package com.farming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SessionConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Allow session cookies
        config.setAllowCredentials(true);

        // ✅ Explicit frontend origins (MANDATORY for credentials)
        config.setAllowedOrigins(List.of(
                "https://narayanjagtap.github.io",      // GitHub Pages
                "http://localhost:5500",               // Local testing
                "http://127.0.0.1:5500"
        ));

        // ✅ Allow headers & methods
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}

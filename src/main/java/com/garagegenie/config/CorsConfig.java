package com.garagegenie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${FRONTEND_URL:}")
    private String frontendUrl;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        List<String> allowedOrigins = new ArrayList<>(Arrays.asList(
                // ── Production ──────────────────────────────────────────
                "https://harshgaragesaharanpur.netlify.app",
                // ── Local development ────────────────────────────────────
                "http://localhost:5173",
                "http://localhost:8080",
                "http://localhost:3000",
                "http://localhost:4173"));

        // Additional origins can be injected via FRONTEND_URL Railway variable
        // (comma-separated, e.g. https://a.netlify.app,https://b.netlify.app)
        if (frontendUrl != null && !frontendUrl.isBlank()) {
            Arrays.stream(frontendUrl.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(allowedOrigins::add);
        }

        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

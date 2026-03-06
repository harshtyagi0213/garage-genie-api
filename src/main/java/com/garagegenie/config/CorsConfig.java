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

    // Set FRONTEND_URL env var in Railway to your Netlify URL, e.g.
    // https://garage-genie.netlify.app
    @Value("${FRONTEND_URL:}")
    private String frontendUrl;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Always allow local development origins
        List<String> allowedOrigins = new ArrayList<>(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:8080",
                "http://localhost:3000",
                "http://localhost:4173"));

        // Add the production Netlify URL from environment variable
        if (frontendUrl != null && !frontendUrl.isBlank()) {
            // Support comma-separated list of URLs e.g.
            // "https://a.netlify.app,https://b.netlify.app"
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

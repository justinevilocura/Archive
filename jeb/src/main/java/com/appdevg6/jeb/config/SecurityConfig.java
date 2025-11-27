package com.appdevg6.jeb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Temporary Security Configuration to allow access to /api/events
 * until full user authentication is implemented.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // IMPORTANT: We disable CSRF protection and authorize all requests 
        // to the /api/events path so you can test the CRUD functionality.
        http
            .csrf(csrf -> csrf.disable()) // Disable Cross-Site Request Forgery
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/events/**").permitAll() // Allow all requests to our API
                .anyRequest().authenticated() // Protect everything else
            );
        return http.build();
    }
}
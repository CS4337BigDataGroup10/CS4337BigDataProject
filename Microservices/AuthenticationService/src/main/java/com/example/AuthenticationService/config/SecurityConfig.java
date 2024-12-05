package com.example.AuthenticationService.config;

import com.example.AuthenticationService.config.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF protection since it's not needed for APIs
                .authorizeHttpRequests(authorize -> authorize
                        // Allow unauthenticated access to `/auth/grantcode`
                        .requestMatchers("/auth/grantcode").permitAll()
                        // Require JWT authentication for `/auth/user` and `/auth/refresh`
                        .requestMatchers("/auth/user", "/auth/refresh").authenticated()
                        // All other endpoints require authentication by default
                        .anyRequest().authenticated()
                )
                // Add the JwtFilter before the default UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

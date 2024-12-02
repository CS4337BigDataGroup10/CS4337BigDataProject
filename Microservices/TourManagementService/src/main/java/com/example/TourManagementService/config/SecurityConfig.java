package com.example.TourManagementService.config;

import com.example.TourManagementService.config.filter.JwtFilter;
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
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // Allow unauthenticated access to create new tours (example use case)
                        .requestMatchers("/tours/create").permitAll()

                        // Authenticate access to sensitive endpoints
                        .requestMatchers(
                                "/tours/available",
                                "/tours/{tourId}",
                                "/tours/{tourId}/update-participant-count",
                                "/tours/{tourId}/self-assign",
                                "/tours/{tourId}/self-deassign",
                                "/tours/{tourId}/addBooking",
                                "/tours/{tourId}/removeBooking"
                        ).authenticated()

                        // Default rule: require authentication for all other requests
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

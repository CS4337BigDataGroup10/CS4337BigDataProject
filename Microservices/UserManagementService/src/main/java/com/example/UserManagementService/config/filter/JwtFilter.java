package com.example.UserManagementService.config.filter;

import com.example.UserManagementService.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Check if the Authorization header contains a Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7); // Extract the token

            try {
                // Validate the token
                Claims claims = jwtService.validateToken(jwtToken);
                // Optionally, set up security context if needed
                SecurityContextHolder.getContext().setAuthentication(null);
            } catch (Exception e) {
                // Log or handle invalid token
                logger.warn("JWT validation failed: " + e.getMessage());
            }
        }

        // Proceed with the next filter
        filterChain.doFilter(request, response);
    }

}

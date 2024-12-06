package com.example.AuthenticationService.config.filter;

import com.example.AuthenticationService.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

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

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7); // Extract the token

            try {
                jwtService.validateToken(jwtToken);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken("", null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired JWT: " + e.getMessage());
                return;
            }
        } else if (requiresAuthentication(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header missing or invalid");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/user") || path.startsWith("/auth/refresh");
    }
}

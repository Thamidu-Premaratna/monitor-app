package com.monitor.api.utils;

/*

    SIT Group project : monitor-app v1.0

   ------------------ JWT Filter ----------------------
    * This class is used to define a custom JWT filter
    * It extends the OncePerRequestFilter class
    * It overrides the doFilterInternal method to check the JWT token
    * It checks if the token is valid and if the user is authenticated
    * If the token is valid, it sets the Security Context with the authentication token
    * If the token is not valid, it sends an unauthorized response
    * It allows requests to the auth endpoint without authentication
    * All other requests require authentication


 */

import com.monitor.api.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) { // If the request is to the auth endpoint, let it pass
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // If the Authorization header is not present or does not start with Bearer let it pass
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // Check if the token is valid and if the user is authenticated
        // If the Security Context is already set, let the request pass
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // If the username is not null and the user is not authenticated
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Check if the token is valid
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Set the Security Context with the authentication token
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // If the token is not valid, send an unauthorized response
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is not valid");
            }
        }
        // Continue with the filter chain after checking the token
        filterChain.doFilter(request, response);
    }
}

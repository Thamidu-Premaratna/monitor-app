package com.monitor.api.utils.config;

/*
    SIT Group project : monitor-app v1.0

    ------------------ Security Configuration ----------------------
    (Spring Security Configuration)
    * This will define security configurations for the monitor app
    * It defines a security filter chain to secure the application
    * It uses an authentication provider to authenticate users
    * It uses a custom JWT filter to check JWT before the UsernamePasswordAuthenticationFilter
    * It allows requests to the auth endpoint without authentication
    * All other requests require authentication
    * It sets the session creation policy to STATELESS

 */

import com.monitor.api.utils.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtAuthFilter; // Custom JWT filter to check JWT before the UsernamePasswordAuthenticationFilter

    // Define a security filter chain to secure the application
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/auth/**" // Allow requests to the auth endpoint without authentication
                                ).permitAll()
                                .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)// Before UsernamePasswordAuthenticationFilter, check JWT first using the jwt filter
                .formLogin(withDefaults());
        return http.build();
    }
}

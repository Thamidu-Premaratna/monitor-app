package com.monitor.api.utils.config;

/*
    SIT Group project : monitor-app v1.0

    ------------------ Beans Configuration ----------------------
    (Spring Beans Configuration)
    * This class is used to define beans (which are objects managed by the Spring IoC container)
    * Define a bean for authentication provider
    * Define a bean for password encoder
    * Define a bean for authentication manager

 */

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {

    private final UserDetailsService userDetailsService;

    // Bean for authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider(){ // Authentication provider bean for DaoAuthenticationProvider
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Set user details service
        authProvider.setPasswordEncoder(passwordEncoder()); // Set password encoder bean
        return authProvider;
    }

    // Define a bean for password encoder
    @Bean
    public PasswordEncoder passwordEncoder() { // Password encoder bean for BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

    // Bean for authentication manager. An AuthenticationManager is used to process an Authentication request.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}

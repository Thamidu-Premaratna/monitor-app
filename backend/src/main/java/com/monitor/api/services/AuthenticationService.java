package com.monitor.api.services;

import com.monitor.api.dto.request.AuthenticationRequest;
import com.monitor.api.dto.request.RegistrationRequest;
import com.monitor.api.dto.response.AuthenticationResponse;
import com.monitor.api.repository.RoleRepository;
import com.monitor.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager; // Authenticate the user using the AuthenticationManager

    private final JwtService jwtService;

    public void register(RegistrationRequest request){
        // Check if the user already exists
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("User already exists");
        }

        var user = com.monitor.api.model.User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findByName(request.getRole()).orElseThrow(()-> new RuntimeException("Role not found")))
                .accountLocked(false)
                .enabled(true)
                .build(); // Create a new user
        userRepository.save(user); // Save the use

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) { // Authenticate the user and return the token
        var auth = authenticationManager.authenticate( // Authenticate the user
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        var claims = new HashMap<String,Object>(); // Claims to be added to the token payload
        var user = ((User)auth.getPrincipal()); // Get the user from the authentication object returned
        // A claim is a piece of information asserted about a subject and is used to provide the subject with specific rights or privileges.

        claims.put("email", user.getUsername());
        claims.put("authorities", user.getAuthorities());
        var jwtToken = jwtService.generateToken(claims, user); // Generate the token
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .message("User authenticated successfully")
                .build();
    }
}
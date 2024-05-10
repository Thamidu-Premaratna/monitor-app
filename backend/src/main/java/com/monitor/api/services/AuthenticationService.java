package com.monitor.api.services;


import com.monitor.api.model.Token;
import com.monitor.api.repository.RoleRepository;
import com.monitor.api.repository.TokenRepository;
import com.monitor.api.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager; // Authenticate the user using the AuthenticationManager

    private final EmailService emailService;
    private final JwtService jwtService;

    // Activation URL
    @Value("${spring.application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(()-> new IllegalStateException("ROLE USER was not initialized"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false) // Account not by default locked
                .enabled(false) // Account not enable, user should enable it themselves
                .roles(List.of(userRole))
                .build();
        userRepository.save(user); // Save the use

        sendValidationEmail(user);

    }

    // Generate/Save and Send validation token to Email
    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveAuthenticationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    // Generate the Activation token(code)
    private String generateAndSaveAuthenticationToken(User user) { // Generate the token and save it to the database and return the token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789"; // characters used to generate the random code
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom(); // Make sure the generated String is secure cryptographically

        //Generate the random code
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length()); // 0 to characters.length() - 1 ( 0 - 9 )
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString(); // Return the generated code
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

        claims.put("fullName", user.getFullName());
        claims.put("email", user.getEmail());
        var jwtToken = jwtService.generateToken(claims, user); // Generate the token
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token) // Find the token by the token code sent
                .orElseThrow(()-> new RuntimeException("Token not found"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())) { // Check if the token has expired
            sendValidationEmail(savedToken.getUser()); // Send a new token to the user email
            throw new RuntimeException("Activation Token expired. A new token has been send to the same email address again.");
        }
        var user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(() -> new UsernameNotFoundException("User not found")); // Get the user from the token
        user.setEnabled(true); // Enable the user account
        userRepository.save(user); // Save the user
        savedToken.setValidatedAt(LocalDateTime.now()); // Set the token as validated
        tokenRepository.save(savedToken); // Save the token
    }
}

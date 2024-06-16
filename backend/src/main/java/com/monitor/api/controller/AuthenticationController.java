package com.monitor.api.controller;

import com.monitor.api.dto.request.AuthenticationRequest;
import com.monitor.api.dto.request.RegistrationRequest;
import com.monitor.api.dto.response.AuthenticationResponse;
import com.monitor.api.exceptions.UserAlreadyExistsException;
import com.monitor.api.exceptions.UserNotFoundException;
import com.monitor.api.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth") // Set the base path for the controller. This is enough since we have added /api/v1 in the application.yml file
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    )throws UserAlreadyExistsException {
        authenticationService.register(request);
        return ResponseEntity.created(null).build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) throws UserNotFoundException {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}

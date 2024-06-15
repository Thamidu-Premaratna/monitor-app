package com.monitor.api.exceptions;

import com.monitor.api.dto.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // User not found exception
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotFoundException exp) { // Handle UserNotFoundException and return a response
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // Return 404 Not Found status
                .body(
                        ExceptionResponse
                                .builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    // User already exists exception
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserAlreadyExistsException exp) { // Handle UserAlreadyExistsException and return a response
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // Return 409 Conflict status
                .body(
                        ExceptionResponse
                                .builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    // When the user has both account locked and disabled - User access denied
    @ExceptionHandler(UserAccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserAccessDeniedException exp) { // Handle UserAccessDeniedException and return a response
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN) // Return 403 Forbidden status
                .body(
                        ExceptionResponse
                                .builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    // Handle JWT expired exception
    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handleException(io.jsonwebtoken.ExpiredJwtException exp) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // Return 401 Unauthorized status
                .body(
                        ExceptionResponse
                                .builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    // handle UserNotAuthorizedException
    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotAuthorizedException exp) { // Handle UserNotAuthorizedException and return a response
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // Return 403 Forbidden status
                .body(
                        ExceptionResponse
                                .builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    // Handle account locked exception
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp) { // Handle LockedException and return a response
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // Return 401 Unauthorized status
                .body(
                        ExceptionResponse
                                .builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    // Handle account disabled exception
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp) { // Handle DisabledException and return a response
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // Return 401 Unauthorized status
                .body(
                        ExceptionResponse
                                .builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    // Handle bad credentials exception
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exp) { // Handle BadCredentialsException and return a response
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // Return 401 Unauthorized status
                .body(
                        ExceptionResponse
                                .builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    // Handle MethodArgumentNotValidException for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp) { // This will be thrown by the @Valid annotation in the controller
        Set<String> validationErrors = new HashSet<>(); // Create a set to store validation errors because there may be errors with the same message ex: "Field is required" in multiple instances.
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                            validationErrors.add(error.getDefaultMessage()); // Add the error message to the set
                        }
                );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // Return 400 Bad Request status
                .body(
                        ExceptionResponse
                                .builder()
                                .validationErrors(validationErrors)
                                .build()
                );
    }

    //Handle NoSuchAlgorithmException
    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<ExceptionResponse> handleException(NoSuchAlgorithmException exp) { // Handle NoSuchAlgorithmException and return a response
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // Return 500 Internal Server Error status
                .body(
                        ExceptionResponse
                                .builder()
                                .error("Internal Server Error (500): " + exp.getMessage())
                                .build()
                );
    }

    // Handled RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleException(RuntimeException exp) { // Handle RuntimeException and return a response

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // Return 500 Internal Server Error status
                .body(
                        ExceptionResponse
                                .builder()
                                .error("Internal Server Error (500): " + exp.getMessage())
                                .build()
                );
    }

    // Handle fallback for any other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp) { // This will handle any other exceptions

        // log the exception (development)
        exp.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // Return 500 Internal Server Error status
                .body(
                        ExceptionResponse
                                .builder()
                                .error("Internal Server Error (500): " + exp.getMessage())
                                .build()
                );
    }
}

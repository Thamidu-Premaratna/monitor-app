package com.monitor.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields when serializing to JSON
public class ExceptionResponse {
    private String error; // Error message (e.g. "User not found")
    private Set<String> validationErrors; // Validation errors , Set<String> is used to prevent duplicate errors.
    private Map<String, String> errors; // Other errors that are not validation errors
}

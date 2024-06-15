package com.monitor.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "First name is required")
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name should have max 50 characters")
    private String firstname;

    @Size(max = 50, message = "Last name should have max 50 characters")
    @NotEmpty(message = "Last name is required")
    @NotBlank(message = "Last name is required")
    private String lastname;


    @Size(min = 5, message = "Email should have at least 5 characters")
    @Size(max = 100, message = "Email should have max 100 characters")
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password should have a between of 8 and 50 characters")
    private String password;

    @NotEmpty(message = "Role is required")
    @NotBlank(message = "Role is required")
    private String role;
}

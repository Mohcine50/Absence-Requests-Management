package com.shegami.securityJwt.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthManager {

    @NotBlank(message = "Username is empty")
    @Size(min = 3, max = 15, message = "Username length should be between 3 and 15 chars")
    @NotNull(message = "Username is empty")
    private String username;

    @NotNull(message = "Username is empty")
    @NotBlank(message = "Password is empty")
    @Size(min = 8, message = "Weak Password")
    private String password;
}

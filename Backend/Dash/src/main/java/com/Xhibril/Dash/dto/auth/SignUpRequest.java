package com.Xhibril.Dash.dto.auth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignUpRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 32, message = "Password must be 8–32 characters long")
    @Pattern(
            regexp = "^$|^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.]).+$",
            message = "Password must contain 1 uppercase, 1 number, and 1 special character"
    )
    private String password;


    public String getEmail(){ return email; }
    public String getPassword(){ return password; }
}

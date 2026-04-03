package com.Xhibril.Dash.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdatePasswordRequest {


    @NotBlank(message = "Password is required")
    private String oldPassword;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 32, message = "Password must be 8–32 characters long")

    @Pattern(
            regexp = "^$|^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.]).+$",
            message = "Password must contain 1 uppercase, 1 number, and 1 special character"
    )
    private String newPassword;

    private String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}

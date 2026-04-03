package com.Xhibril.Dash.dto.auth;
import com.Xhibril.Dash.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PasswordResetRequest {

    @NotBlank(message = "Email is required", groups = ValidationGroups.Step1.class)
    @Email(message = "Enter a valid email address", groups = ValidationGroups.Step1.class)
    private String email;

    @NotBlank(message = "Verification code is required", groups = ValidationGroups.Step2.class)
    @Pattern(regexp = "^\\d{6}$", message = "Code must be 6 digits", groups = ValidationGroups.Step2.class)
    private String code;

    @NotBlank(message = "Password is required", groups = ValidationGroups.Step3.class)
    @Size(min = 8, max = 32, message = "Password must be 8–32 characters long", groups = ValidationGroups.Step3.class)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.]).+$",
            message = "Password must contain 1 uppercase, 1 number, and 1 special character", groups = ValidationGroups.Step3.class
    )
    private String newPassword;

    private String confirmPassword;

    @NotBlank(message = "Reset token is required", groups = ValidationGroups.Step3.class)
    private String resetToken;

    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setCode(String code) { this.code = code;}
    public String getCode(){ return code;}

    public String getNewPassword(){ return newPassword;}

    public String getConfirmPassword(){ return confirmPassword; }


    public void setResetToken(String resetToken){ this.resetToken = resetToken;}
    public String getResetToken(){ return resetToken;}
}

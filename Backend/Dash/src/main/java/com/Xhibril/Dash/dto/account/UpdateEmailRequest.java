package com.Xhibril.Dash.dto.account;
import com.Xhibril.Dash.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateEmailRequest {

    @NotBlank(message = "Email is required", groups = ValidationGroups.Step1.class)
    @Email(message = "Enter a valid email address", groups = ValidationGroups.Step1.class)
    private String pendingEmail;

    @NotBlank(message = "Verification code is required", groups = ValidationGroups.Step2.class)
    @Pattern(regexp = "^\\d{6}$", message = "Code must be 6 digits", groups = ValidationGroups.Step2.class)
    private String code;

    @NotBlank( message = "Password is required", groups = ValidationGroups.Step1.class)
    @Size(min = 8, max = 32, message = "Password must be 8–32 characters long", groups = ValidationGroups.Step1.class)
    @Pattern(
            regexp = "^$|^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.]).+$",
            message = "Password must contain 1 uppercase, 1 number, and 1 special character"
    )
    private String password;

    @NotBlank(message = "Reset token is required", groups = ValidationGroups.Step3.class)
    private String resetToken;

    public String getPendingEmail() {
        return pendingEmail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setResetToken(String resetToken){ this.resetToken = resetToken;}
    public String getResetToken(){ return resetToken;}
}

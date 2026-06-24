package com.odonto.odonto_system.user;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequest(

        @NotBlank(message = "A nova senha não pode estar em branco")
        String newPassword

) {
}

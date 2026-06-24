package com.odonto.odonto_system.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(

        @NotBlank(message = "O nome completo é obrigatório")
        String fullName,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotNull(message = "O cargo (role) é obrigatório")
        UserRole role

) {
}

package com.odonto.odonto_system.patient;

import com.odonto.odonto_system.shared.util.Cpf;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PatientRequest (

    @NotBlank(message = "O Nome Completo é obrigatório")
    @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
    String fullName,

    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos")
    @Cpf
    String cpf,

    @NotNull @Past(message = "A data de nascimento deve ser uma data passada")
    LocalDate dateOfBirth,

    @Size(max = 20, message = "O telefone é muito longo")
    String phone,

    @Email(message = "E-mail inválido")
    String email,

    String address,

    String notes

){}

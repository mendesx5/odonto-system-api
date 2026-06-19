package com.odonto.odonto_system.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequest(
        @NotNull(message = "O ID do paciente é obrigatório")
        UUID patientId,

        @NotNull(message = "O ID do dentista é obrigatório")
        UUID dentistId,

        @NotNull(message = "O horário de início é obrigatório")
        @Future(message = "O horário de início deve ser uma data futura")
        LocalDateTime startTime,

        @NotNull(message = "O horário de término é obrigatório")
        @Future(message = "O horário de término deve ser uma data futura")
        LocalDateTime endTime,

        @Size(max = 300, message = "O motivo não pode exceder 300 caracteres")
        String reason,

        String notes
) {}

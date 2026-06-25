package com.odonto.odonto_system.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record BlockScheduleRequest(

        @NotNull(message = "O ID do dentista é obrigatório")
        UUID dentistId,

        @NotNull(message = "O horário de início é obrigatório")
        LocalDateTime startTime,

        @NotNull(message = "O horário de término é obrigatório")
        LocalDateTime endTime,

        @NotBlank(message = "A justificativa do bloqueio (motivo) é obrigatória")
        String reason

) { }

package com.odonto.odonto_system.appointment;

import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
        @NotNull(message = "O novo status é obrigatório")
        AppointmentStatus status
) {
}

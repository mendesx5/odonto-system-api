package com.odonto.odonto_system.tooth;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ToothRecordRequest(

        @NotNull(message = "O número do dente é obrigatório")
        @Min(value = 11, message = "Número de dente inválido")
        @Max(value = 85, message = "Número de dente inválido")
        Integer toothNumber,

        @NotNull(message = "O status do dente é obrigatório")
        ToothStatus status,

        UUID clinicalRecordId,
        String notes

) {
}

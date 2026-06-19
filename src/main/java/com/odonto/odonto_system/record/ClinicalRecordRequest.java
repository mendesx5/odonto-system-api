package com.odonto.odonto_system.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record ClinicalRecordRequest(
        @NotNull(message = "O ID do paciente é obrigatório")
        UUID patientId,

        @NotNull(message = "A data do registo é obrigatória")
        LocalDate recordDate,

        @NotBlank(message = "A queixa principal é obrigatória")
        String chiefComplaint,

        String diagnosis,
        String treatmentPlan,
        String evolutionNotes
) {}
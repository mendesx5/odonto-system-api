package com.odonto.odonto_system.record;

import com.odonto.odonto_system.procedure.ProcedureResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ClinicalRecordResponse(
        UUID id,
        UUID patientId,
        String patientName,
        UUID dentistId,
        String dentistName,
        LocalDate recordDate,
        String chiefComplaint,
        String diagnosis,
        String treatmentPlan,
        String evolutionNotes,
        boolean active,
        List<ProcedureResponse>  procedures,
        LocalDateTime createdAt
) {
    public ClinicalRecordResponse(ClinicalRecord record) {
        this (
                record.getId(),
                record.getPatient().getId(),
                record.getPatient().getFullName(),
                record.getDentist().getId(),
                record.getDentist().getFullName(),
                record.getRecordDate(),
                record.getChiefComplaint(),
                record.getDiagnosis(),
                record.getTreatmentPlan(),
                record.getEvolutionNotes(),
                record.isActive(),
                record.getProcedures() != null ?
                        record.getProcedures().stream().map(ProcedureResponse::new).toList() : List.of(),
                record.getCreatedAt()
        );
    }
}
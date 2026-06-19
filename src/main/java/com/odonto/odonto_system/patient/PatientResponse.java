package com.odonto.odonto_system.patient;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public record PatientResponse (
        UUID id,
        String fullName,
        String cpf,
        LocalDate dateOfBirth,
        int age,
        String phone,
        String email,
        String address,
        String notes,
        boolean active
) {
    public PatientResponse (Patient patient) {
        this(
                patient.getId(),
                patient.getFullName(),
                patient.getCpf(),
                patient.getDateOfBirth(),
                Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getNotes(),
                patient.isActive()
        );
    }
}
package com.odonto.odonto_system.patient;

import java.util.UUID;

public record RecurrenceResponse(
        UUID patientId,
        String fullName,
        String phone,
        String messageTemplate
) {
    public RecurrenceResponse(Patient pacient, String dentistName) {
        this(
                pacient.getId(),
                pacient.getFullName(),
                pacient.getPhone(),
                String.format("Olá %s, faz 6 meses que você realizou sua última limpeza preventivo com o %s. Vamos agendar seu retorno para manter seu sorriso saudável?",
                        pacient.getFullName(), dentistName)
        );
    }
}

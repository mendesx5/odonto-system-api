package com.odonto.odonto_system.tooth;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ToothRecordResponse(
        UUID id,
        int toothNumber,
        ToothStatus status,
        String notes,
        String dentistName,
        LocalDate recordedAt,
        LocalDateTime createdAt
) {
    public ToothRecordResponse(ToothRecord record) {
        this(
                record.getId(),
                record.getToothNumber(),
                record.getStatus(),
                record.getNotes(),
                record.getDentist().getFullName(),
                record.getRecordedAt(),
                record.getCreatedAt()
        );
    }
}

package com.odonto.odonto_system.appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        UUID patientId,
        String patientName,
        UUID dentistId,
        String dentistName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AppointmentStatus status,
        String reason,
        String notes,
        LocalDateTime createdAt
) {
    public AppointmentResponse(Appointment appointment) {
        this(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getPatient().getFullName(),
                appointment.getDentist().getId(),
                appointment.getDentist().getFullName(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getStatus(),
                appointment.getReason(),
                appointment.getNotes(),
                appointment.getCreatedAt()
        );
    }
}

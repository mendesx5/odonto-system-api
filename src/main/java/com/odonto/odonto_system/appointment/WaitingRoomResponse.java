package com.odonto.odonto_system.appointment;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public record WaitingRoomResponse(
        UUID appointmentId,
        String patientName,
        String dentistName,
        AppointmentStatus status,
        LocalDateTime startTime,
        Long waitingTimeMinutes
) {
    public WaitingRoomResponse(Appointment appointment) {
        this(
                appointment.getId(),
                appointment.getPatient().getFullName(),
                appointment.getDentist().getFullName(),
                appointment.getStatus(),
                appointment.getStartTime(),
                appointment.getArrivedAt() != null ?
                        ChronoUnit.MINUTES.between(appointment.getArrivedAt(), LocalDateTime.now()) : 0L
        );
    }
}

package com.odonto.odonto_system.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE " +
            "a.dentist.id = :dentistId AND " +
            "a.id != :excludeId AND " +
            "a.status NOT IN ('CANCELADO', 'NAO_COMPARECEU') AND " +
            "a.startTime < :endTime AND " +
            "a.endTime > :startTime")

    boolean existsConflict(@Param("dentistId") UUID dentistId,
                           @Param("startTime") LocalDateTime startTime,
                           @Param("endTime")   LocalDateTime endTime,
                           @Param("excludeId") UUID excludeId);

}

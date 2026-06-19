package com.odonto.odonto_system.appointment;

import com.odonto.odonto_system.patient.Patient;
import com.odonto.odonto_system.shared.BaseEntity;
import com.odonto.odonto_system.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id",  nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id",  nullable = false)
    private User dentist;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(length = 300)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String notes;

}

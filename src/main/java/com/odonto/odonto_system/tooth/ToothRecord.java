package com.odonto.odonto_system.tooth;

import com.odonto.odonto_system.patient.Patient;
import com.odonto.odonto_system.record.ClinicalRecord;
import com.odonto.odonto_system.shared.BaseEntity;
import com.odonto.odonto_system.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tooth_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToothRecord  extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinical_record_id")
    private ClinicalRecord clinicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id", nullable = false)
    private User dentist;

    @Column(name = "tooth_number", nullable = false)
    private int toothNumber; // Numeração FDI (11 a 85)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ToothStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "recorded_at", nullable = false)
    private LocalDate recordedAt;

    @Column(nullable = false)
    private boolean active = true;

}

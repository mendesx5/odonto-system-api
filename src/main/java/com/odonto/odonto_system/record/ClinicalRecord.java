package com.odonto.odonto_system.record;

import com.odonto.odonto_system.patient.Patient;
import com.odonto.odonto_system.shared.BaseEntity;
import com.odonto.odonto_system.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "clinical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicalRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id",  nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id",  nullable = false)
    private User dentist;

    // Mapeado como UUID temporariamente
    @Column(name = "appointment_id")
    private UUID appointmentId;

    @Column(name = "record_date",  nullable = false)
    private LocalDate recordDate;

    @Column(name = "chief_complaint", columnDefinition = "TEXT")
    private String chiefComplaint;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "treatment_plan", columnDefinition = "TEXT")
    private String treatmentPlan;

    @Column(name = "evolution_notes", columnDefinition = "TEXT")
    private String evolutionNotes;

    @Column(nullable = false)
    private boolean active = true;

}

package com.odonto.odonto_system.exam;

import com.odonto.odonto_system.patient.Patient;
import com.odonto.odonto_system.record.ClinicalRecord;
import com.odonto.odonto_system.shared.BaseEntity;
import com.odonto.odonto_system.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "exam_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamFile extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinical_record_id")
    private ClinicalRecord clinicalRecord;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "storage_key", nullable = false, length = 500)
    private String storageKey;

    @Column(name = "public_url", nullable = false, length = 500)
    private String publicUrl;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "file_size_bytes", nullable = false)
    private Long fileSizeBytes;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type", nullable = false, length = 50)
    private ExamType examType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "exam_date")
    private LocalDate examDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(nullable = false)
    private boolean active;

}

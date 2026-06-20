package com.odonto.odonto_system.exam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExamFileResponse (
        UUID id,
        UUID patientId,
        UUID clinicalRecordId,
        String originalName,
        String publicUrl,
        String contentType,
        Long fileSizeBytes,
        ExamType examType,
        String description,
        LocalDate examDate,
        UUID uploadedById,
        String uploadedByName,
        LocalDateTime createdAt
) {
    public ExamFileResponse(ExamFile examFile) {
        this(
                examFile.getId(),
                examFile.getPatient().getId(),
                examFile.getClinicalRecord() != null ? examFile.getClinicalRecord().getId() : null,
                examFile.getOriginalName(),
                examFile.getPublicUrl(),
                examFile.getContentType(),
                examFile.getFileSizeBytes(),
                examFile.getExamType(),
                examFile.getDescription(),
                examFile.getExamDate(),
                examFile.getUploadedBy().getId(),
                examFile.getUploadedBy().getFullName(),
                examFile.getCreatedAt()
        );
    }
}

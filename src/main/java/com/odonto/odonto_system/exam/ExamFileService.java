package com.odonto.odonto_system.exam;

import com.odonto.odonto_system.patient.Patient;
import com.odonto.odonto_system.patient.PatientRepository;
import com.odonto.odonto_system.record.ClinicalRecord;
import com.odonto.odonto_system.record.ClinicalRecordRepository;
import com.odonto.odonto_system.shared.exception.ConflictException;
import com.odonto.odonto_system.shared.exception.ResourceNotFoundException;
import com.odonto.odonto_system.shared.storage.StorageResult;
import com.odonto.odonto_system.shared.storage.StorageService;
import com.odonto.odonto_system.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamFileService {

    private final ExamFileRepository examFileRepository;
    private final PatientRepository patientRepository;
    private final ClinicalRecordRepository clinicalRecordRepository;
    private final StorageService storageService;

    // Upload do arquivo e Salvamento de Metadados
    @Transactional
    public ExamFileResponse upload(UUID patientId, UUID clinicalRecordId, ExamType examType,
                                   String description, LocalDate examDate, MultipartFile file) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado. ID: " + patientId));
        if (!patient.isActive()) {
            throw new ConflictException("Não é possível associar exames a um paciente inativado.");
        }

        ClinicalRecord record = null;
        if (clinicalRecordId != null) {
            record = clinicalRecordRepository.findByIdAndActiveTrue(clinicalRecordId)
                    .orElseThrow(() -> new ResourceNotFoundException("Prontuário clínico não encontrado ou inativo."));
        }

        User uploadedBy = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        StorageResult storageResult = storageService.upload(file, patientId);

        ExamFile examFile = ExamFile.builder()
                .patient(patient)
                .clinicalRecord(record)
                .originalName(file.getOriginalFilename())
                .storageKey(storageResult.storageKey())
                .publicUrl(storageResult.publicUrl())
                .contentType(file.getContentType())
                .fileSizeBytes(file.getSize())
                .examType(examType != null ? examType : ExamType.OUTRO)
                .description(description)
                .examDate(examDate != null ? examDate : LocalDate.now())
                .uploadedBy(uploadedBy)
                .active(true)
                .build();

        ExamFile savedExamFile = examFileRepository.save(examFile);
        return new ExamFileResponse(savedExamFile);
    }

    // Buscar exame individual por ID
    @Transactional
    public ExamFileResponse findById(UUID id) {
        ExamFile examFile = examFileRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exame não encontrado ou inativo. ID: " + id));
        return new ExamFileResponse(examFile);
    }

    // Listar exames de um Paciente
    @Transactional
    public Page<ExamFileResponse> findByPatientId(UUID patientId, Pageable pageable) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Paciente não encontrado. ID: " + patientId);
        }
        return examFileRepository.findByPatientIdAndActiveTrue(patientId, pageable)
                .map(ExamFileResponse::new);
    }

    // Deletar exame
    @Transactional
    public void delete(UUID id) {
        ExamFile examFile = examFileRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exame não encontrado ou já inativado. ID: " + id));

        storageService.delete(examFile.getStorageKey());

        examFile.setActive(false);
        examFileRepository.save(examFile);
    }

}

package com.odonto.odonto_system.tooth;

import com.odonto.odonto_system.patient.Patient;
import com.odonto.odonto_system.patient.PatientRepository;
import com.odonto.odonto_system.record.ClinicalRecord;
import com.odonto.odonto_system.record.ClinicalRecordRepository;
import com.odonto.odonto_system.shared.exception.ResourceNotFoundException;
import com.odonto.odonto_system.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToothRecordService {

    private final ToothRecordRepository toothRecordRepository;
    private final PatientRepository patientRepository;
    private final ClinicalRecordRepository clinicalRecordRepository;

    // Registra o status de um dente (Histórico Imutável)
    @Transactional
    public ToothRecordResponse recordStatus(UUID patientId, ToothRecordRequest request) {
        Patient patient = patientRepository.findByIdAndActiveTrue(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado ou inativo. ID: " + patientId));

        ClinicalRecord record = null;
        if (request.clinicalRecordId() != null) {
            record = clinicalRecordRepository.findByIdAndActiveTrue(request.clinicalRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prontuário clínico não encontrado ou inativo. ID: " + request.clinicalRecordId()));
        }

        User dentist = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        ToothRecord toothRecord = ToothRecord.builder()
                .patient(patient)
                .clinicalRecord(record)
                .dentist(dentist)
                .toothNumber(request.toothNumber())
                .status(request.status())
                .notes(request.notes())
                .recordedAt(LocalDate.now())
                .active(true)
                .build();

        return new ToothRecordResponse(toothRecordRepository.save(toothRecord));
    }

    // Odontograma Atual Completo
    @Transactional
    public OdontogramResponse getOdontogram(UUID patientId) {
        Patient patient = patientRepository.findByIdAndActiveTrue(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado ou inativo. ID: " + patientId));

        // Busca o estado mais recente de cada dente ativo
        List<ToothRecord> currentTeeth = toothRecordRepository.findCurrentOdontogram(patientId);

        List<ToothRecordResponse> teethResponses = currentTeeth.stream()
                .map(ToothRecordResponse::new)
                .toList();

        // Calcula a data da última atualização geral do mapa bucal
        LocalDateTime lastUpdated = currentTeeth.stream()
                .map(ToothRecord::getCreatedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        // Identifica o último dentista que alterou o mapa
        String updatedBy = currentTeeth.stream()
                .max(Comparator.comparing(ToothRecord::getCreatedAt))
                .map(t -> t.getDentist().getFullName())
                .orElse("Sem registros");

        return new OdontogramResponse(
                patient.getId(),
                patient.getFullName(),
                teethResponses,
                lastUpdated,
                updatedBy
        );
    }

    // Histórico de evolução cronológica de um dente específico
    @Transactional
    public List<ToothRecordResponse> getHistory(UUID patientId, int toothNumber) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Paciente não encontrado. ID: " + patientId);
        }

        return toothRecordRepository.findByPatientIdAndToothNumberAndActiveTrueOrderByCreatedAtDesc(patientId, toothNumber)
                .stream()
                .map(ToothRecordResponse::new)
                .toList();
    }

}

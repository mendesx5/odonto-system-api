package com.odonto.odonto_system.record;

import com.odonto.odonto_system.patient.Patient;
import com.odonto.odonto_system.patient.PatientRepository;
import com.odonto.odonto_system.procedure.ProcedureCatalog;
import com.odonto.odonto_system.procedure.ProcedureCatalogRepository;
import com.odonto.odonto_system.shared.exception.ConflictException;
import com.odonto.odonto_system.shared.exception.ResourceNotFoundException;
import com.odonto.odonto_system.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicalRecordService {

    private final ClinicalRecordRepository clinicalRecordRepository;
    private final PatientRepository patientRepository;
    private final ProcedureCatalogRepository procedureCatalogRepository;

    // Criar Prontuário
    @Transactional
    public ClinicalRecordResponse create (ClinicalRecordRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        if (!patient.isActive()) {
            throw new ConflictException("Não é possível registrar um prontuário para um paciente inativo.");
        }

        User dentist = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        List<ProcedureCatalog> procedures = resolveProcedures(request.procedureIds());

        ClinicalRecord clinicalRecord = ClinicalRecord.builder()
                .patient(patient)
                .dentist(dentist)
                .recordDate(request.recordDate())
                .chiefComplaint(request.chiefComplaint())
                .diagnosis(request.diagnosis())
                .treatmentPlan(request.treatmentPlan())
                .evolutionNotes(request.evolutionNotes())
                .procedures(procedures)
                .active(true)
                .build();

        ClinicalRecord savedRecord = clinicalRecordRepository.save(clinicalRecord);
        return new ClinicalRecordResponse(savedRecord);
    }

    // Listar prontuário de um paciente específico
    @Transactional
    public Page<ClinicalRecordResponse> findByPatientId (UUID patientId, Pageable pageable) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Paciente não encontrado com o ID: " + patientId);
        }

        return clinicalRecordRepository.findByPatientIdAndActiveTrue(patientId, pageable)
                .map(ClinicalRecordResponse::new);
    }

    // Buscar prontuário pelo id
    @Transactional
    public ClinicalRecordResponse findById (UUID id) {
        ClinicalRecord clinicalRecord = clinicalRecordRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        return new ClinicalRecordResponse(clinicalRecord);
    }

    // Atualizar prontuário
    @Transactional
    public ClinicalRecordResponse update (UUID id, ClinicalRecordRequest request) {
        ClinicalRecord clinicalRecord = clinicalRecordRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuário não encontrado com o ID: " + id));

        clinicalRecord.setRecordDate(request.recordDate());
        clinicalRecord.setChiefComplaint(request.chiefComplaint());
        clinicalRecord.setDiagnosis(request.diagnosis());
        clinicalRecord.setTreatmentPlan(request.treatmentPlan());
        clinicalRecord.setEvolutionNotes(request.evolutionNotes());

        List<ProcedureCatalog> procedures = resolveProcedures(request.procedureIds());
        clinicalRecord.setProcedures(procedures);

        ClinicalRecord savedRecord = clinicalRecordRepository.save(clinicalRecord);
        return new ClinicalRecordResponse(savedRecord);
    }

    // Soft delete do prontuário
    @Transactional
    public void softDelete (UUID id) {
        ClinicalRecord clinicalRecord = clinicalRecordRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuário não encontrado com o ID: " + id));

        clinicalRecord.setActive(false);
        clinicalRecordRepository.save(clinicalRecord);
    }

    // Busca os ProcedureCatalog pelos IDs
    private List<ProcedureCatalog> resolveProcedures(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return procedureCatalogRepository.findAllById(ids);
    }

}

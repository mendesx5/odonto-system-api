package com.odonto.odonto_system.patient;

import com.odonto.odonto_system.shared.exception.ConflictException;
import com.odonto.odonto_system.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    // Criar Paciente
    @Transactional
    public PatientResponse createPatient (PatientRequest patientRequest) {
        if (patientRepository.existsByCpf(patientRequest.cpf())) {
            throw new ConflictException("Já existe um paciente cadastrado com este CPF.");
        }

        Patient patient = Patient.builder()
                .fullName(patientRequest.fullName())
                .cpf(patientRequest.cpf())
                .dateOfBirth(patientRequest.dateOfBirth())
                .phone(patientRequest.phone())
                .email(patientRequest.email())
                .address(patientRequest.address())
                .notes(patientRequest.notes())
                .active(true)
                .build();

        Patient savedPatient = patientRepository.save(patient);

        return new PatientResponse(savedPatient);
    }

    // Atualizar paciente
    @Transactional
    public PatientResponse updatePatient (UUID id, PatientRequest patientRequest) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID: " + id));

        if (!patient.getCpf().equals(patientRequest.cpf()) && patientRepository.existsByCpf(patientRequest.cpf())) {
            throw new ConflictException("Já existe outro paciente cadastrado com este CPF.");
        }

        patient.setFullName(patientRequest.fullName());
        patient.setCpf(patientRequest.cpf());
        patient.setDateOfBirth(patientRequest.dateOfBirth());
        patient.setPhone(patientRequest.phone());
        patient.setEmail(patientRequest.email());
        patient.setAddress(patientRequest.address());
        patient.setNotes(patientRequest.notes());

        return new PatientResponse(patientRepository.save(patient));
    }

    // Deletar paciente (Soft delete)
    @Transactional
    public void deletePatient (UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID: " + id));

        patient.setActive(false);

        patientRepository.save(patient);
    }

    // Listar todos os pacientes
    @Transactional
    public Page<PatientResponse> findAllPatients (String name, String cpf, Pageable pageable) {
        String nameFilter = name != null ? name : "";
        String cpfFilter = cpf != null ? cpf : "";

        return patientRepository
                .findByFullNameOrCpf(nameFilter, cpfFilter, pageable)
                .map(PatientResponse::new);
    }

    // Buscar paciente por id
    @Transactional
    public PatientResponse findById (UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
        return new PatientResponse(patient);
    }

}

package com.odonto.odonto_system.patient;

import com.odonto.odonto_system.shared.exception.ConflictException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private PatientRepository patientRepository;

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



}

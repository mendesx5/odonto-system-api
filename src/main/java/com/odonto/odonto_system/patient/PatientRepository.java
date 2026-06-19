package com.odonto.odonto_system.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByCpf(String cpf);
    Patient findByCpf(String cpf);

    Page<Patient> findByFullNameContainingIgnoreCaseOrCpfContaining(String fullName, String cpf, Pageable pageable);
}

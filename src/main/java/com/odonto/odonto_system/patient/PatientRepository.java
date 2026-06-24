package com.odonto.odonto_system.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    @Query("""
    SELECT DISTINCT p FROM Patient p
    JOIN ClinicalRecord cr ON cr.patient = p
    JOIN cr.procedures proc
    WHERE proc.name = 'Profilaxia (limpeza)'
    AND p.active = true
    AND cr.recordDate <= :targetDate
    AND NOT EXISTS (
        SELECT a FROM Appointment a
        WHERE a.patient = p
        AND a.startTime > CURRENT_TIMESTAMP
        AND a.status NOT IN ('CANCELADO', 'NAO_COMPARECEU')
    )
""")
    List<Patient> findPatientsForRecurrence(@Param("targetDate") java.time.LocalDate targetDate);

    @Query("SELECT p FROM Patient p WHERE p.active = true AND " +
            "(LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%')) OR p.cpf LIKE CONCAT('%', :cpf, '%'))")
    Page<Patient> findActivePatients(@Param("name") String name, @Param("cpf") String cpf, Pageable pageable);

    Optional<Patient> findByIdAndActiveTrue(UUID id);

    boolean existsByCpf(String cpf);
    boolean existsByCpfAndActiveTrue(String cpf);

}

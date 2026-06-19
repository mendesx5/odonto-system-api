package com.odonto.odonto_system.record;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicalRecordRepository extends JpaRepository<ClinicalRecord, UUID> {
    Page<ClinicalRecord> findByPatientIdAndActiveTrue(UUID id, Pageable pageable);

    Optional<ClinicalRecord> findByPatientIdAndActiveTrue(UUID id);
}

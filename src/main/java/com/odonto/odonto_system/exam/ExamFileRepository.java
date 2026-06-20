package com.odonto.odonto_system.exam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamFileRepository extends JpaRepository<ExamFile, Long> {

    Page<ExamFile> findByPatientIdAndActiveTrue (UUID patientId, Pageable pageable);

    Optional<ExamFile> findByIdAndActiveTrue (UUID id);

}

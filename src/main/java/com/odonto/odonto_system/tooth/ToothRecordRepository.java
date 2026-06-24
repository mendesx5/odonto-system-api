package com.odonto.odonto_system.tooth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ToothRecordRepository extends JpaRepository<ToothRecord, UUID> {

    @Query("""
        SELECT t FROM ToothRecord t
        WHERE t.patient.id = :patientId
        AND t.active = true
        AND t.createdAt = (
            SELECT MAX(t2.createdAt) FROM ToothRecord t2
            WHERE t2.patient.id = :patientId
            AND t2.toothNumber = t.toothNumber
            AND t2.active = true
        )
        ORDER BY t.toothNumber
        """)
    List<ToothRecord> findCurrentOdontogram(@Param("patientId") UUID patientId);

    List<ToothRecord> findByPatientIdAndToothNumberAndActiveTrueOrderByCreatedAtDesc(UUID patientId, int toothNumber);
}

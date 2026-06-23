package com.odonto.odonto_system.procedure;

import com.odonto.odonto_system.record.ClinicalRecord;
import com.odonto.odonto_system.record.ClinicalRecordRepository;
import com.odonto.odonto_system.record.ClinicalRecordResponse;
import com.odonto.odonto_system.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcedureService {

    private final ProcedureCatalogRepository procedureCatalogRepository;
    private final ClinicalRecordRepository clinicalRecordRepository;

    // Listar catálogo de procedimentos ativos
    @Transactional
    public List<ProcedureResponse> findAllActive () {
        return procedureCatalogRepository.findByActiveTrue().stream()
                .map(ProcedureResponse::new)
                .toList();
    }

    // Vincular procedimentos a um prontuário
    @Transactional
    public ClinicalRecordResponse bindProceduresToRecord (UUID recordId, ProcedureRequest procedureRequest) {
        ClinicalRecord clinicalRecord = clinicalRecordRepository.findByIdAndActiveTrue(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuário clínico não encontrado ou inativo."));

        List<ProcedureCatalog> selectedProcedures = procedureCatalogRepository.findAllById(procedureRequest.procedureIds());

        if (selectedProcedures.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum procedimento válido foi encontrado com os IDs fornecidos.");
        }

        clinicalRecord.getProcedures().addAll(selectedProcedures);

        return new ClinicalRecordResponse(clinicalRecordRepository.save(clinicalRecord));
    }

}

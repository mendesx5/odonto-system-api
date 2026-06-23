package com.odonto.odonto_system.procedure;

import com.odonto.odonto_system.record.ClinicalRecordResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/procedures")
@RequiredArgsConstructor
public class ProcedureController {

    private final ProcedureService procedureService;

    @GetMapping("/catalog")
    public ResponseEntity<List<ProcedureResponse>> getCatalog() {
        return ResponseEntity.ok(procedureService.findAllActive());
    }

    @PostMapping("/records/{recordId}")
    public ResponseEntity<ClinicalRecordResponse> bindToRecord (
            @PathVariable UUID recordId,
            @Valid @RequestBody ProcedureRequest request
    ) {
        return ResponseEntity.ok(procedureService.bindProceduresToRecord(recordId, request));
    }

}

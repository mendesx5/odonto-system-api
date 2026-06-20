package com.odonto.odonto_system.record;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ClinicalRecordController {

    private final ClinicalRecordService clinicalRecordService;

    @PostMapping("/patients/{patientId}/records")
    public ResponseEntity<ClinicalRecordResponse> create(
            @PathVariable UUID patientId,
            @Valid @RequestBody ClinicalRecordRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicalRecordService.create(request));
    }

    @GetMapping("/patients/{patientId}/records")
    public ResponseEntity<Page<ClinicalRecordResponse>> findByPatientId(
            @PathVariable(required = false) UUID patientId,
            @PageableDefault(size = 10, sort = "recordDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ClinicalRecordResponse> page = clinicalRecordService.findByPatientId(patientId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/records/{id}")
    public ResponseEntity<ClinicalRecordResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(clinicalRecordService.findById(id));
    }

    @PutMapping("/records/{id}")
    public ResponseEntity<ClinicalRecordResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ClinicalRecordRequest request
    ) {
        return ResponseEntity.ok(clinicalRecordService.update(id, request));
    }

    @DeleteMapping("/records/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clinicalRecordService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

}

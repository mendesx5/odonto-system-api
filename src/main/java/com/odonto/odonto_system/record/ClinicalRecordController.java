package com.odonto.odonto_system.record;

import com.odonto.odonto_system.patient.PatientResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clinical")
@RequiredArgsConstructor
public class ClinicalRecordController {

    private final ClinicalRecordService clinicalRecordService;

    @PostMapping
    public ResponseEntity<ClinicalRecordResponse> create(@Valid @RequestBody ClinicalRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicalRecordService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicalRecordResponse> update(@PathVariable UUID id, @Valid @RequestBody ClinicalRecordRequest request) {
        return ResponseEntity.ok(clinicalRecordService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clinicalRecordService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ClinicalRecordResponse>> findAll(
            @RequestParam(required = false) UUID patientId,
            @PageableDefault(size = 10, sort = "fullName") Pageable pageable
    ) {
        Page<ClinicalRecordResponse> page = clinicalRecordService.findByPatientId(patientId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalRecordResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(clinicalRecordService.findById(id));
    }

}

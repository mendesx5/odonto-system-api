package com.odonto.odonto_system.patient;

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
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(request));
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponse>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cpf,
            @PageableDefault(size = 10, sort = "fullName") Pageable pageable
            ) {
        Page<PatientResponse> page = patientService.findAllPatients(name, cpf, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> update(@PathVariable UUID id, @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

}

package com.odonto.odonto_system.patient;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<PatientResponse>> findAll() {
        return ResponseEntity.ok(patientService.findAllPatients());
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

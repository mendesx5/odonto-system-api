package com.odonto.odonto_system.tooth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients/{patientId}/odontogram")
@RequiredArgsConstructor
public class ToothRecordController {

    private final ToothRecordService toothRecordService;

    // Registrar ou atualizar a condição clínica de um dente específico
    @PostMapping
    public ResponseEntity<ToothRecordResponse> recordStatus(
            @PathVariable UUID patientId,
            @Valid @RequestBody ToothRecordRequest request
    ) {
        ToothRecordResponse response = toothRecordService.recordStatus(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Buscar o odontograma
    @GetMapping
    public ResponseEntity<OdontogramResponse> getOdontogram(@PathVariable UUID patientId) {
        return ResponseEntity.ok(toothRecordService.getOdontogram(patientId));
    }

    // Buscar o histórico de um único dente
    @GetMapping("/{toothNumber}/history")
    public ResponseEntity<List<ToothRecordResponse>> getToothHistory(
            @PathVariable UUID patientId,
            @PathVariable int toothNumber
    ) {
        return ResponseEntity.ok(toothRecordService.getHistory(patientId, toothNumber));
    }

}

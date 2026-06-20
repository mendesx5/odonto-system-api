package com.odonto.odonto_system.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ExamFileController {

    private final ExamFileService examFileService;

    @PostMapping(value = "/patients/{patientId}/exams", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExamFileResponse> upload(
            @PathVariable UUID patientId,
            @RequestParam(required = false) UUID clinicalRecordId,
            @RequestParam(required = false) ExamType examType,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate examDate,
            @RequestParam("file") MultipartFile file
    ) {
        ExamFileResponse response = examFileService.upload(
                patientId, clinicalRecordId, examType, description, examDate, file
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/exams/{id}")
    public ResponseEntity<ExamFileResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(examFileService.findById(id));
    }

    @GetMapping("/patients/{patientId}/exams")
    public ResponseEntity<Page<ExamFileResponse>> findByPatientId(
            @PathVariable UUID patientId,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(examFileService.findByPatientId(patientId, pageable));
    }

    @DeleteMapping("/exams/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examFileService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

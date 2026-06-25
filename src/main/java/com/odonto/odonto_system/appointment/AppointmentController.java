package com.odonto.odonto_system.appointment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentResponse>> findAll(
            @PageableDefault(size = 20, sort = "startTime") Pageable pageable
    ) {
        return ResponseEntity.ok(appointmentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentRequest request
    ) {
        return ResponseEntity.ok(appointmentService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody StatusUpdateRequest request
    ) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, request.status()));
    }

    @GetMapping("/waiting-room")
    public ResponseEntity<List<WaitingRoomResponse>> getWaitingRoom() {
        return ResponseEntity.ok(appointmentService.getWaitingRoom());
    }

    @PostMapping("/block")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')") // Garante controle de acesso
    public ResponseEntity<AppointmentResponse> createBlock(@Valid @RequestBody BlockScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createBlock(request));
    }

}

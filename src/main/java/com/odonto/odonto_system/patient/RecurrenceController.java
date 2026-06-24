package com.odonto.odonto_system.patient;

import com.odonto.odonto_system.notification.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/recurrence")
@RequiredArgsConstructor
public class RecurrenceController {

    private final PatientRepository patientRepository;
    private final WhatsAppService whatsAppService;

    // Lista todos os pacientes para retorno
    @GetMapping("/list")
    public ResponseEntity<List<RecurrenceResponse>> getReturnList() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        List<RecurrenceResponse> response = patientRepository.findPatientsForRecurrence(sixMonthsAgo).stream()
                .map(p -> new RecurrenceResponse(p, "Dr. Igor"))
                .toList();
        return ResponseEntity.ok(response);
    }

    // Dispara o WhatsApp ativo com um clique
    @PostMapping("/trigger")
    public ResponseEntity<Void> triggerNotification(@RequestParam String phone, @RequestBody String message) {
        whatsAppService.sendMessage(phone, message);
        return ResponseEntity.noContent().build();
    }

}

package com.odonto.odonto_system.appointment;

import com.odonto.odonto_system.notification.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final AppointmentRepository appointmentRepository;
    private final WhatsAppService whatsAppService;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendAppointmentReminders () {
        System.out.println("Motor de lembretes automático iniciado");

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startTomorrow = tomorrow.atStartOfDay();
        LocalDateTime endTomorrow = tomorrow.atTime(23, 59, 59);

        List<Appointment> tomorrowAppointments = appointmentRepository.findAppointmentsByTimeRange(startTomorrow, endTomorrow);

        List<Appointment> elegibleAppointments = tomorrowAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.AGENDADO || a.getStatus() == AppointmentStatus.CONFIRMADO)
                .toList();

        System.out.println("Encontrados " + elegibleAppointments.size() + " lembretes elegíveis para amanhã.");

        for (Appointment a : elegibleAppointments) {
            if (a.getPatient() != null) {
                String phone = a.getPatient().getPhone();
                String patientName = a.getPatient().getFullName();
                String dentistName = a.getDentist().getFullName();

                String timeStr = a.getStartTime().toLocalTime().toString();

                String message = String.format(
                        "Olá, %s! Passando para lembrar da sua consulta odontológica agendada para amanhã, dia %s às %s, com o %s. Confirmamos sua presença? Responder com SIM ou NÃO.",
                        patientName, tomorrow.toString(), timeStr, dentistName
                );

                whatsAppService.sendMessage(phone, message);
            }
        }

        System.out.println("Disparos em lotes finalizados.");

    }

}

package com.odonto.odonto_system.appointment;

import com.odonto.odonto_system.patient.Patient;
import com.odonto.odonto_system.patient.PatientRepository;
import com.odonto.odonto_system.shared.exception.ConflictException;
import com.odonto.odonto_system.shared.exception.ResourceNotFoundException;
import com.odonto.odonto_system.user.User;
import com.odonto.odonto_system.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    // Criar consulta
    @Transactional
    public AppointmentResponse create (AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));

        User dentist = userRepository.findById(request.dentistId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado."));

        // Validação de horário
        UUID temporaryId = UUID.randomUUID();
        if (appointmentRepository.existsConflict(request.dentistId(), request.startTime(), request.endTime(), temporaryId)) {
            throw new ConflictException("Conflito de agenda: O dentista já possui um compromisso neste horário.");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .dentist(dentist)
                .startTime(request.startTime())
                .endTime(request.endTime())
                .status(AppointmentStatus.AGENDADO)
                .reason(request.reason())
                .notes(request.notes())
                .build();

        return new AppointmentResponse(appointmentRepository.save(appointment));
    }

    // Listar Todas as Consultas
    @Transactional
    public Page<AppointmentResponse> findAll (Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(AppointmentResponse::new);
    }

    // Buscar por id
    @Transactional
    public AppointmentResponse findById (UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));
        return new AppointmentResponse(appointment);
    }

    // Atualizar consulta
    @Transactional
    public AppointmentResponse update (UUID id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));

        if (appointmentRepository.existsConflict(request.dentistId(), request.startTime(), request.endTime(), id)) {
            throw new ConflictException("Conflito de agenda: O dentista já possui um compromisso neste horário.");
        }

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));
        User dentist = userRepository.findById(request.dentistId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado."));

        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(request.endTime());
        appointment.setReason(request.reason());
        appointment.setNotes(request.notes());

        return new AppointmentResponse(appointmentRepository.save(appointment));
    }

    // Mudar Status
    @Transactional
    public AppointmentResponse updateStatus(UUID id, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));

        appointment.setStatus(newStatus);
        return new AppointmentResponse(appointmentRepository.save(appointment));
    }

}

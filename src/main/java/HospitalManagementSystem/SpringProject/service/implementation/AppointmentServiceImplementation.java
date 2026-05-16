package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.Appointment;
import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.AppointmentStatus;
import HospitalManagementSystem.SpringProject.record.AppointmentRecord;
import HospitalManagementSystem.SpringProject.repository.AppointmentRepository;
import HospitalManagementSystem.SpringProject.repository.DoctorRepository;
import HospitalManagementSystem.SpringProject.repository.PatientRepository;
import HospitalManagementSystem.SpringProject.service.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static HospitalManagementSystem.SpringProject.entity.Status.AppointmentStatus.*;

@RequiredArgsConstructor
@Service
@Transactional
public class AppointmentServiceImplementation implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    @Override
    public Appointment bookAppointment(Appointment appointment) {
        // Validate patient exists
        Patient patient = patientRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Check if doctor is available
        if (isDoctorAvailable(doctor.getId(), appointment.getAppointmentDateTime())) {
            throw new RuntimeException("Doctor is not available at this time");
        }
        appointment.setCreatedAt(LocalDateTime.now());

        return appointmentRepository.save(appointment);
    }

    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findByIdWithDetails(id);
    }

    @Override
    public Page<Appointment> getAllAppointments() {
        PageRequest page = PageRequest.of(0, 100);
        return appointmentRepository.findAll(page);
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        PageRequest page = PageRequest.of(0, 50); // Limit to 50 per query
        return appointmentRepository.findByPatientId(patientId,page).getContent();
    }

//    @Override
//    public List<AppointmentDTO> getAppointmentsByDoctor(Long doctorId) {
//        PageRequest page = PageRequest.of(0, 50);
//        return appointmentRepository.findByDoctorIdWithPatient(doctorId,page).getContent();
//    }

    // AppointmentService.java
    public List<AppointmentRecord> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findAppointmentRecordsByDoctor(doctorId);
    }

    @Override
    public Page<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        PageRequest page = PageRequest.of(0, 50);
        return appointmentRepository.findByStatus(status,page);
    }

    @Override
    public List<Appointment> getTodayAppointments() {
        LocalDateTime start = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        return appointmentRepository.findByappointmentDateTimeBetween(start, end);
    }

    @Override
    public List<Appointment> getDoctorAppointmentsByDate(Long doctorId, LocalDate date) {
        return appointmentRepository.findDoctorAppointmentsByDate(doctorId, date);
    }

    @Override
    @Transactional
    public Appointment rescheduleAppointment(Long id, LocalDateTime newDateTime) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Check if new time slot is available
        if (isDoctorAvailable(appointment.getDoctor().getId(), newDateTime)) {
            throw new RuntimeException("Doctor not available at requested time");
        }

        appointment.setAppointmentDateTime(newDateTime);
        appointment.setStatus(RESCHEDULED);

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(CANCELLED);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(COMPLETED);
        return appointmentRepository.save(appointment);
    }

    @Override
    public boolean isDoctorAvailable(Long doctorId, LocalDateTime dateTime) {
        List<Appointment> existing = appointmentRepository
                .findByDoctorIdAndappointmentDateTime(doctorId, dateTime);

        return !existing.isEmpty();
    }

    @Override
    public long getAppointmentCountForToday() {
        LocalDateTime start = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        return appointmentRepository.findByappointmentDateTimeBetween(start, end).size();
    }
}

package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.Appointment;
import HospitalManagementSystem.SpringProject.entity.Status.AppointmentStatus;
import HospitalManagementSystem.SpringProject.record.AppointmentRecord;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    // Create
    Appointment bookAppointment(Appointment appointment);

    // Read
    Optional<Appointment> getAppointmentById(Long id);

    List<AppointmentRecord> getAppointmentsByDoctor(Long doctorId);

    Page<Appointment> getAllAppointments();

    List<Appointment> getAppointmentsByPatient(Long patientId);

    Page<Appointment> getAppointmentsByStatus(AppointmentStatus status);

    List<Appointment> getTodayAppointments();

    List<Appointment> getDoctorAppointmentsByDate(Long doctorId, LocalDate date);

    // Update
    Appointment rescheduleAppointment(Long id, LocalDateTime newDateTime);

    Appointment cancelAppointment(Long id);

    Appointment completeAppointment(Long id);

    // Business methods
    boolean isDoctorAvailable(Long doctorId, LocalDateTime dateTime);

    long getAppointmentCountForToday();
}

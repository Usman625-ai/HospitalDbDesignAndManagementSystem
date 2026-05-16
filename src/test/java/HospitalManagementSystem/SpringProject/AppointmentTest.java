package HospitalManagementSystem.SpringProject;

import HospitalManagementSystem.SpringProject.entity.Appointment;
import HospitalManagementSystem.SpringProject.entity.Status.AppointmentStatus;
import HospitalManagementSystem.SpringProject.record.AppointmentRecord;
import HospitalManagementSystem.SpringProject.service.AppointmentService;
import HospitalManagementSystem.SpringProject.service.DoctorService;
import HospitalManagementSystem.SpringProject.service.PatientService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static HospitalManagementSystem.SpringProject.entity.Status.AppointmentStatus.SCHEDULED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AppointmentTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    // ========== CREATE ==========
    @Test
    void bookAppointmentTest() {
        // Create a new appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patientService.getPatientById(5L).orElse(null)); // Assuming patient exists
        appointment.setDoctor(doctorService.getDoctorById(32L).orElse(null));  // Assuming doctor exists
        appointment.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
        appointment.setStatus(SCHEDULED);
        appointment.setSymptoms("Fever and cough");
        appointment.setNotes("Patient needs immediate attention");

        Appointment saved = appointmentService.bookAppointment(appointment);

        assertThat(saved.getId()).isNotNull();
        System.out.println("✅ Booked appointment ID: " + saved.getId());
    }

    // ========== READ ==========
    @Test
    @Transactional
    void getAppointmentByIDTest() {
        long start = System.currentTimeMillis();
        // Assuming appointment with ID 1 exists
        Appointment appointment = appointmentService.getAppointmentById(5L).orElse(null);
        long time = System.currentTimeMillis() - start;

        if (appointment != null) {
            System.out.println("getAppointmentById(): " + time + "ms - Found appointment for patient: " + appointment.getPatient().getPatientId());
        } else {
            System.out.println("getAppointmentById(): " + time + "ms - No appointment found with ID 1");
        }
    }

    @Test
    void getAppointmentByDoctorTest() {
        long start = System.currentTimeMillis();
        List<AppointmentRecord> appointments = appointmentService.getAppointmentsByDoctor(32L);
        long time = System.currentTimeMillis() - start;

        System.out.println("getAppointmentsByDoctor(): " + time + "ms (" + appointments.size() + " records)");
        assertThat(appointments).isNotEmpty();
    }

    @Test
    void getAppointmentByPatientTest() {
        long start = System.currentTimeMillis();
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(1L);
        long time = System.currentTimeMillis() - start;

        System.out.println("getAppointmentsByPatient(): " + time + "ms (" + appointments.size() + " records)");
    }

    @Test
    void getAppointmentByStatusTest() {
        long start = System.currentTimeMillis();
        Page<Appointment> appointments = appointmentService.getAppointmentsByStatus(SCHEDULED);
        long time = System.currentTimeMillis() - start;

        System.out.println("getAppointmentsByStatus(): " + time + "ms (" + appointments.getTotalElements() + " records)");
    }

    @Test
    void getTodayAppointmentsTest() {
        long start = System.currentTimeMillis();
        List<Appointment> appointments = appointmentService.getTodayAppointments();
        long time = System.currentTimeMillis() - start;

        System.out.println("getTodayAppointments(): " + time + "ms (" + appointments.size() + " records)");
    }

    // ========== UPDATE ==========
    @Test
    void rescheduleAppointmentTest() {
        long start = System.currentTimeMillis();
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(5);
        Appointment updated = appointmentService.rescheduleAppointment(1L, newDateTime);
        long time = System.currentTimeMillis() - start;

        if (updated != null) {
            System.out.println("rescheduleAppointment(): " + time + "ms - New date: " + updated.getAppointmentDateTime());
        } else {
            System.out.println("rescheduleAppointment(): " + time + "ms - Appointment not found");
        }
    }

    @Test
    void cancelAppointmentTest() {
        long start = System.currentTimeMillis();
        Appointment cancelled = appointmentService.cancelAppointment(1L);
        long time = System.currentTimeMillis() - start;

        if (cancelled != null) {
            System.out.println("cancelAppointment(): " + time + "ms - Status: " + cancelled.getStatus());
        } else {
            System.out.println("cancelAppointment(): " + time + "ms - Appointment not found");
        }
    }

    @Test
    void completeAppointmentTest() {
        long start = System.currentTimeMillis();
        Appointment completed = appointmentService.completeAppointment(2L);
        long time = System.currentTimeMillis() - start;

        if (completed != null) {
            System.out.println("completeAppointment(): " + time + "ms - Status: " + completed.getStatus());
        } else {
            System.out.println("completeAppointment(): " + time + "ms - Appointment not found");
        }
    }
}
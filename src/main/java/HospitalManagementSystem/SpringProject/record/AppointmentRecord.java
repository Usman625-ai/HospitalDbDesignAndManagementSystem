package HospitalManagementSystem.SpringProject.record;


import java.time.LocalDateTime;

public interface AppointmentRecord{
    Long id();
    LocalDateTime appointmentDateTime();
    String status();
    String patientFirstName();
    String patientLastName();
    String doctorName();
}

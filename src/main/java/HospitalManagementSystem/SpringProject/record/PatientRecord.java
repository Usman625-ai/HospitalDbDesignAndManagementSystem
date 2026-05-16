package HospitalManagementSystem.SpringProject.record;

import java.time.LocalDateTime;

public interface PatientRecord {
    String patientId();
    String firstName();
    String lastName();
    String email();
    String status();
    LocalDateTime registeredAt();
}
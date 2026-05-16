package HospitalManagementSystem.SpringProject.record;

import java.time.LocalDateTime;

public interface PrescriptionRecord {
    Long getId();
    String getPrescriptionNumber();
    String getDiagnosis();
    String getStatus();
    String getPatientName();
    String getDoctorName();
    LocalDateTime getPrescriptionDate();
}
package HospitalManagementSystem.SpringProject.record;

import java.time.LocalDateTime;

public interface BillRecord {
    Long getId();
    Double getTotalAmount();
    String getPaymentStatus();
    LocalDateTime getBillDate();
    String getPatientName();
    String getDoctorName();
}
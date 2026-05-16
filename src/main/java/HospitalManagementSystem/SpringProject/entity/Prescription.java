package HospitalManagementSystem.SpringProject.entity;

import HospitalManagementSystem.SpringProject.entity.Status.PrescriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "prescription", indexes = {
        @Index(name = "idx_patient_id", columnList = "patient_id"),
        @Index(name = "idx_prescriptionNumber", columnList = "prescriptionNumber"),
        @Index(name = "idx_doctor_id", columnList = "doctor_id"),
        @Index(name = "idx_appointment_id", columnList = "appointment_id"),
        @Index(name = "idx_prescription_date", columnList = "prescription_date")
})
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, insertable = false, updatable = false)
    private String prescriptionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PrescriptionMedicine> prescriptionMedicines = new ArrayList<>();

    // Diagnosis
    @Column(length = 1000)
    private String diagnosis;

    @Column(name = "investigations", length = 1000)
    private String investigations;  // Tests to be done

    // Instructions
    @Column(name = "general_advice", length = 1000)
    private String generalAdvice;

    // Validity
    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    // Dates
    @Column(name = "prescription_date", nullable = false)
    private LocalDateTime prescriptionDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Status
    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status = PrescriptionStatus.ACTIVE;

    // Pharmacy
    @Column(name = "pharmacy_name", length = 100)
    private String pharmacyName;

    @Column(name = "created_by")
    private String createdBy;

    // Helper method to add medicine
    public void addMedicine(Medicine medicine, String dosage, String frequency, String duration, Integer quantity) {
        PrescriptionMedicine pm = new PrescriptionMedicine();
        pm.setPrescription(this);
        pm.setMedicine(medicine);
        pm.setDosage(dosage);
        pm.setFrequency(frequency);
        pm.setDuration(duration);
        pm.setQuantity(quantity);
        pm.setIsSubstituteAllowed(true);
        prescriptionMedicines.add(pm);
    }
}

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
        @Index(name = "idx_doctor_id", columnList = "doctor_id"),
        @Index(name = "idx_appointment_id", columnList = "appointment_id"),
        @Index(name = "idx_prescription_date", columnList = "prescription_date")
})
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String prescriptionNumber;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    // Medications (OneToMany to separate table for better design)
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medicine> medicines = new ArrayList<>();

    // Diagnosis
    @Column(length = 1000)
    private String diagnosis;

    @Column(name = "investigations", length = 1000)
    private String investigations;  // Tests to be done

    // Instructions
    @Column(name = "general_advice", length = 1000)
    private String generalAdvice;

    @Column(name = "follow_up_instructions", length = 500)
    private String followUpInstructions;

    // Follow up
    @Column(name = "follow_up_required")
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;

    @Column(name = "follow_up_duration_days")
    private Integer followUpDurationDays;

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
}

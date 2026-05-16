package HospitalManagementSystem.SpringProject.entity;

import HospitalManagementSystem.SpringProject.entity.Status.MedicalRecordType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "medical_record", indexes = {
        @Index(name = "idx_patient_id", columnList = "patient_id"),
        @Index(name = "idx_doctor_id", columnList = "doctor_id"),
        @Index(name = "idx_appointment_id", columnList = "appointment_id"),
        @Index(name = "idx_record_no", columnList = "recordNumber"),
        @Index(name = "idx_recordDate", columnList = "recordDate")
})
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            insertable = false,
            updatable = false)
    private String recordNumber;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private MedicalRecordType recordType; // GENERAL, EMERGENCY, FOLLOW_UP, SURGERY

    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String treatment;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String bloodPressure;

    private String heartRate;

    private String temperature;

    private String weight;

    private String height;

    private String allergies;

    private String pastSurgeries;

    private String familyHistory;

    @CreationTimestamp
    private LocalDateTime recordDate;
}
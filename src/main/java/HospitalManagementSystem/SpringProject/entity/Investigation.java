package HospitalManagementSystem.SpringProject.entity;

import HospitalManagementSystem.SpringProject.entity.Status.InvestigationStatus;
import HospitalManagementSystem.SpringProject.entity.Status.InvestigationType;
import HospitalManagementSystem.SpringProject.entity.Status.UrgencyStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "investigation", indexes = {
        @Index(name = "idx_patient_id", columnList = "patient_id"),
        @Index(name = "idx_doctor_id", columnList = "doctor_id"),
        @Index(name = "idx_investigation_requestedDate", columnList = "requestedDate"),
        @Index(name = "idx_investigation_resultDate", columnList = "resultDate")
})
public class Investigation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private InvestigationType investigationType;

    private String description;

    private UrgencyStatus urgency;

    private InvestigationStatus status;

    @Column(columnDefinition = "TEXT")
    private String results;

    private String reportUrl;

    @CreationTimestamp
    private LocalDateTime requestedDate;

    private LocalDateTime resultDate;

    private String notes;
}
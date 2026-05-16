package HospitalManagementSystem.SpringProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "prescription_medicine")
public class PrescriptionMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    // Prescription-specific fields
    private String dosage;      // "500mg"
    private String frequency;   // "Twice daily"
    private String duration;    // "7 days"
    private Integer quantity;
    private String instructions;
    private Boolean isSubstituteAllowed;

    @CreationTimestamp
    private LocalDateTime prescribedAt;
}
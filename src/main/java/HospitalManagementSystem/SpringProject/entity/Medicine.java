package HospitalManagementSystem.SpringProject.entity;

import HospitalManagementSystem.SpringProject.entity.Status.MedicineCategory;
import HospitalManagementSystem.SpringProject.entity.Status.MedicineStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "prescription_medicine")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @Column(nullable = false, length = 100)
    private String medicineName;

    @Column
    private MedicineStatus status = MedicineStatus.ACTIVE;

    private MedicineCategory category; // TABLET, SYRUP, INJECTION, CAPSULE, OINTMENT

    private int reorderLevel;

    @Column(length = 50)
    private String dosage;  // "500mg"

    @Column(name = "Price")
    private Double UnitPrice; // "500 rupees"

    @Column(length = 50)
    private String frequency;  // "Twice daily"

    @Column(length = 50)
    private String duration;  // "5 days"

    @Column(length = 200)
    private String instructions;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "is_substitute_allowed")
    private Boolean isSubstituteAllowed = false;
}

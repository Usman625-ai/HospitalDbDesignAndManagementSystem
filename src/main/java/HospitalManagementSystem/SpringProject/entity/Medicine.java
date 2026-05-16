package HospitalManagementSystem.SpringProject.entity;

import HospitalManagementSystem.SpringProject.entity.Status.MedicineCategory;
import HospitalManagementSystem.SpringProject.entity.Status.MedicineStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "medicine", indexes = {
        @Index(name = "idx_medicine_id", columnList = "id"),
        @Index(name = "idx_medicineName", columnList = "medicineName"),
        @Index(name = "idx_Medicine_UnitPrice", columnList = "UnitPrice")
})
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String medicineName;

    @Enumerated(EnumType.STRING)
    private MedicineStatus status = MedicineStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    private MedicineCategory category;

    private int reorderLevel;

    @Column(length = 50)
    private String dosage;

    @Column(name = "Price")
    private Double unitPrice;  // Use camelCase: unitPrice

    @Column(length = 200)
    private String instructions;

    private Integer quantity;

    @Column(name = "is_substitute_allowed")
    private Boolean isSubstituteAllowed = false;
}
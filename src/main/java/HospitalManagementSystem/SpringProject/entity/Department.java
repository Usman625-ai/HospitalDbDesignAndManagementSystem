package HospitalManagementSystem.SpringProject.entity;

import HospitalManagementSystem.SpringProject.entity.Status.DepartmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "department", indexes = {
        @Index(name = "idx_dept_name", columnList = "name"),
        @Index(name = "idx_hod_id", columnList = "hod_id"),
        @Index(name = "idx_department_openning_close_time", columnList = "openingTime,ClosingTime"),
        @Index(name = "idx_department_createdAt", columnList = "createdAt"),
        @Index(name = "idx_department_annual_budget", columnList = "annualBudget")
})
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    // Location
    @Column(name = "floor_number")
    private Integer floorNumber;

    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hod_id")
    private Doctor headOfDepartment;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Doctor> doctors = new ArrayList<>();

    // Statistics
    @Column(name = "total_beds")
    private Integer totalBeds = 0;

    @Column(name = "available_beds")
    private Integer availableBeds = 0;

    @Column(name = "total_rooms")
    private Integer totalRooms = 0;

    @Column(name = "opd_count")
    private Integer opdCount = 0;  // Outpatient department daily count

    @Column(name = "ipd_count")
    private Integer ipdCount = 0;  // Inpatient department

    // Contact
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", length = 100)
    private String email;

    // Operating hours
    @Column(name = "opening_time")
    private String openingTime;  // "09:00"

    @Column(name = "closing_time")
    private String closingTime;  // "17:00"

    @Column(name = "is_24x7")
    private Boolean is24x7 = false;

    @Column(name = "emergency_available")
    private Boolean emergencyAvailable = false;

    // Status
    @Column(nullable = false)
    private DepartmentStatus Status;

    // Budget & resources
    @Column(name = "annual_budget", precision = 15, scale = 2)
    private java.math.BigDecimal annualBudget;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Audit
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
}
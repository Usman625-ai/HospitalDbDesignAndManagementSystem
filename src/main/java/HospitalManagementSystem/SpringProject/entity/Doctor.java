package HospitalManagementSystem.SpringProject.entity;

import HospitalManagementSystem.SpringProject.entity.Status.DoctorStatus;
import HospitalManagementSystem.SpringProject.entity.Status.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "doctor", indexes = {
        @Index(name = "idx_doctor_email", columnList = "email"),
        @Index(name = "idx_specialization", columnList = "specialization"),
        @Index(name = "idx_department_id", columnList = "department_id")
})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal information
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 20)
    private String mobileNumber;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // Professional information
    @Column(nullable = false, unique = true, length = 50)
    private String registrationNumber;  // Medical council registration

    @Column(length = 100)
    private String specialization;

    @Column(length = 100)
    private String subSpecialization;

    @Column(name = "qualification", length = 500)
    private String qualification;  // MBBS, MD, MS, etc.

    @Column(name = "experience_years")
    private Integer experienceYears = 0;

    @Column(length = 1000)
    private String achievements;

    @Column(length = 1000)
    private String biography;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    private List<Bill> bills = new ArrayList<>();

    // Schedule
    @Column(name = "consultation_fee", precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Column(name = "follow_up_fee", precision = 10, scale = 2)
    private BigDecimal followUpFee;

    @Column(name = "emergency_fee", precision = 10, scale = 2)
    private BigDecimal emergencyFee;

    @Column(name = "working_days", length = 100)
    private String workingDays;  // "MON,TUE,WED"

    @Column(name = "start_time")
    private String startTime;  // "09:00"

    @Column(name = "end_time")
    private String endTime;  // "17:00"

    @Column(name = "max_patients_per_day")
    private Integer maxPatientsPerDay = 20;

    // Status
    @Column(nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    private DoctorStatus status = DoctorStatus.AVAILABLE;

    // Ratings
    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "patient_count")
    private Integer patientCount = 0;

    // Address
    @Column(name = "clinic_address", length = 500)
    private String clinicAddress;

    @Column(name = "chamber_number", length = 50)
    private String chamberNumber;

    // Emergency contact
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_number", length = 20)
    private String emergencyContactNumber;

    // Timestamps
    @CreationTimestamp
    @Column(name = "joining_date", updatable = false)
    private LocalDateTime joiningDate;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // Audit
    @Column(name = "created_by")
    private String createdBy;
}

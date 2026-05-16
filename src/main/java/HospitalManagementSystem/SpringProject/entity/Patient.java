package HospitalManagementSystem.SpringProject.entity;

import HospitalManagementSystem.SpringProject.entity.Status.PatientStatus;
import HospitalManagementSystem.SpringProject.entity.Status.Gender;
import HospitalManagementSystem.SpringProject.entity.Status.BloodGroup;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(indexes = {
        @Index(name = "idx_patient_status", columnList = "status"),
        @Index(name = "idx_patient_bloodgroup", columnList = "bloodGroup"),
        @Index(name = "idx_patient_email", columnList = "email"),
        @Index(name = "idx_patient_dateofbirth", columnList = "dateOfBirth"),
        @Index(name = "idx_patient_status_registered", columnList = "status, registered_at")
})
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            insertable = false,
            updatable = false)
    private String patientId;

    // Personal information
    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(length = 100)
    private String middleName;

    private String address;

    private String emergencyContact;

    @Column(columnDefinition = "TEXT")
    private String medicalHistory;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    // Status
    @Enumerated(EnumType.STRING)
    private PatientStatus status = PatientStatus.ACTIVE;

    // Relationships
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Prescription> prescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @OneToMany(mappedBy = "patient",fetch = FetchType.LAZY)
    private List<Bill> bills = new ArrayList<>();

}
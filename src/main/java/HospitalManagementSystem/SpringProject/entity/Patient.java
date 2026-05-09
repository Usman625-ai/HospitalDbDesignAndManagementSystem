package HospitalManagementSystem.SpringProject.entity;

import HospitalManagementSystem.SpringProject.entity.Status.PatientStatus;
import HospitalManagementSystem.SpringProject.entity.Status.Gender;
import HospitalManagementSystem.SpringProject.entity.Status.BloodGroup;
import jakarta.persistence.*;
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
@Table(name = "patient", indexes = {
        @Index(name = "idx_patient_email", columnList = "email"),
        @Index(name = "idx_phone_number", columnList = "phoneNumber"),
        @Index(name = "idx_patient_status", columnList = "status")
})
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String patientId;  // e.g., "PAT2024001"

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
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @OneToMany(mappedBy = "patient")
    private List<Bill> bills = new ArrayList<>();

}
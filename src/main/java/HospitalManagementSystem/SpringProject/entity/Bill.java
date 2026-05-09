package HospitalManagementSystem.SpringProject.entity;

import HospitalManagementSystem.SpringProject.entity.Status.PaymentMethod;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(nullable = false)
    private Double consultationFee;

    private Double medicationCharges;

    private Double labCharges;

    private Double roomCharges;

    private Double otherCharges;

    private Double discountAmount;

    private Double taxPercentage;

    private Double totalAmount;

    private PaymentStatus paymentStatus; // PENDING, PAID, OVERDUE

    private PaymentMethod paymentMethod; // CASH, CARD, ONLINE

    @CreationTimestamp
    private LocalDateTime billDate;

    private LocalDateTime paymentDate;

    private LocalDateTime dueDate;

    private String description;
}
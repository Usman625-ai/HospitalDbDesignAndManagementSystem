package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientId(Long patientId);

    List<Prescription> findByDoctorId(Long doctorId);

//    List<Prescription> findByAppointmentId(Long appointmentId);

    List<Prescription> findByStatus(String status);

//    List<Prescription> findByPatientIdOrderByprescriptionDateDesc(Long patientId);

    @Query("SELECT p FROM Prescription p WHERE p.patient.id = :patientId AND p.prescriptionDate BETWEEN :startDate AND :endDate")
    List<Prescription> findPrescriptionsBetweenDates(@Param("patientId") Long patientId,
                                                     @Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Prescription p WHERE p.status = ACTIVE AND p.patient.id = :patientId")
    List<Prescription> findActivePrescriptionsByPatient(@Param("patientId") Long patientId);
}
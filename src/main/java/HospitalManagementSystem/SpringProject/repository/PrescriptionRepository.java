package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Prescription;
import HospitalManagementSystem.SpringProject.record.PrescriptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    @Query("SELECT p.id, p.prescriptionNumber, p.diagnosis, p.status, " +
            "CONCAT(pat.firstName, ' ', pat.lastName), " +
            "doc.name, p.prescriptionDate FROM Prescription p " +
            "JOIN p.patient pat " +
            "JOIN p.doctor doc")
    List<PrescriptionRecord> findAllPrescriptions();

    @Query("SELECT p.id, p.prescriptionNumber, p.diagnosis, p.status, " +
            "CONCAT(pat.firstName, ' ', pat.lastName), " +
            "doc.name, p.prescriptionDate FROM Prescription p " +
            "JOIN p.patient pat " +
            "JOIN p.doctor doc " +
            "WHERE p.patient.id = :patientId")
    List<PrescriptionRecord> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT p.id, p.prescriptionNumber, p.diagnosis, p.status, " +
            "CONCAT(pat.firstName, ' ', pat.lastName), " +
            "doc.name, p.prescriptionDate FROM Prescription p " +
            "JOIN p.patient pat " +
            "JOIN p.doctor doc " +
            "WHERE p.doctor.id = :doctorId")
    List<PrescriptionRecord> findByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT p FROM Prescription p WHERE p.status = 'ACTIVE' AND p.patient.id = :patientId")
    List<Prescription> findActivePrescriptionsByPatient(@Param("patientId") Long patientId);
}
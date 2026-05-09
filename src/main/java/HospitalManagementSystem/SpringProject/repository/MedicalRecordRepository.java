package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.MedicalRecord;
import HospitalManagementSystem.SpringProject.entity.Medicine;
import HospitalManagementSystem.SpringProject.entity.Status.MedicalRecordType;
import HospitalManagementSystem.SpringProject.entity.Status.MedicineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientId(Long patientId);

    List<MedicalRecord> findByPatientIdOrderByRecordDateDesc(Long patientId);

    List<MedicalRecord> findByRecordType(MedicalRecordType recordType);

    @Query("SELECT m FROM MedicalRecord m WHERE m.patient.id = :patientId AND m.recordDate BETWEEN :startDate AND :endDate")
    List<MedicalRecord> findRecordsByDateRange(@Param("patientId") Long patientId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM MedicalRecord m WHERE m.patient.id = :patientId AND m.diagnosis LIKE %:keyword%")
    List<MedicalRecord> searchRecordsByDiagnosis(@Param("patientId") Long patientId, @Param("keyword") String keyword);
}
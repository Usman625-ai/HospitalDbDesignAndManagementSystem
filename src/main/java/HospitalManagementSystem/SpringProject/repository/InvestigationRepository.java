package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Investigation;
import HospitalManagementSystem.SpringProject.entity.Status.InvestigationStatus;
import HospitalManagementSystem.SpringProject.entity.Status.InvestigationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvestigationRepository extends JpaRepository<Investigation, Long> {
    List<Investigation> findByPatientId(Long patientId);

    List<Investigation> findByDoctorId(Long doctorId);

    List<Investigation> findByInvestigationType(InvestigationType investigationType);

    List<Investigation> findByStatus(InvestigationStatus status);

    List<Investigation> findByPatientIdOrderByRequestedDateDesc(Long patientId);

    @Query("SELECT i FROM Investigation i WHERE i.patient.id = :patientId AND i.status = PENDING")
    List<Investigation> findPendingInvestigationsByPatient(@Param("patientId") Long patientId);

    @Query("SELECT i FROM Investigation i WHERE i.requestedDate BETWEEN :start AND :end")
    List<Investigation> findInvestigationsBetweenDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
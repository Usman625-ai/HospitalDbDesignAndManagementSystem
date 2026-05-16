package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.PatientStatus;
import HospitalManagementSystem.SpringProject.record.PatientRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findTopByOrderByIdDesc();

    Optional<Patient> findByEmail(String email);

    @Query("SELECT p.patientId, p.status, p.email,p.firstName,p.lastName, p.registeredAt " +
            "FROM Patient p WHERE p.status = :status")
    List<PatientRecord> findByStatus(@Param("status") PatientStatus status);

    @Query("SELECT p.patientId, p.status, p.email,p.firstName,p.lastName, p.registeredAt " +
            "FROM Patient p WHERE p.lastName = :lastName")
    List<PatientRecord> findByLastNameContaining(@Param("lastName") String lastName);

    boolean existsByEmail(String email);

    @Query("SELECT p.patientId, p.status, p.email, p.firstName,p.lastName, p.registeredAt " +
            "FROM Patient p ORDER BY p.registeredAt DESC")
    Page<PatientRecord> findRecentPatients(Pageable pageable);

    @Query("SELECT p.patientId, p.status, p.email,p.firstName,p.lastName, p.registeredAt " +
            "FROM Patient p")
    List<PatientRecord> findAllPatients();
}
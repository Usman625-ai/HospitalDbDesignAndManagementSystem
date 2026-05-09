package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);

    List<Patient> findByStatus(PatientStatus status);

    List<Patient> findByLastNameContaining(String lastName);

    boolean existsByEmail(String email);

//    will find patients with respect to date, the latest date will be on top in DESC
    @Query("SELECT p FROM Patient p ORDER BY p.registeredAt DESC")
    Page<Patient> findRecentPatients(Pageable pageable);
}
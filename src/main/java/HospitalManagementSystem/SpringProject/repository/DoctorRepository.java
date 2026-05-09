package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);

    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findByDepartment(String department);

    List<Doctor> findByStatus(String status);

    boolean existsByEmail(String email);

    @Query("SELECT d FROM Doctor d ORDER BY d.joiningDate DESC")
    List<Doctor> findRecentDoctors();
}
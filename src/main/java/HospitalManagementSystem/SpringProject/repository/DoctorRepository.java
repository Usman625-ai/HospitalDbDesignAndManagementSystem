package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.DoctorStatus;
import HospitalManagementSystem.SpringProject.record.DoctorRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d.registrationNumber FROM Doctor d ORDER BY d.id DESC LIMIT 1")
    String findMaxDoctorId();

    Optional<Doctor> findByEmail(String email);

    @Query("SELECT d.id as id, d.name as name, " +
            "d.specialization as specialization, " +
            "d.department.name as departmentName, " +
            "d.status as status, " +
            "d.email as email, " +
            "d.registrationNumber as registrationNumber " +
            "FROM Doctor d " +
            "WHERE d.specialization = :specialization")
    List<DoctorRecord> findDoctorBySpecialization(@Param("specialization") String specialization);

    @Query("SELECT d.id as id, d.name as name, " +
            "d.specialization as specialization, " +
            "d.department.name as departmentName, " +
            "d.status as status, " +
            "d.email as email, " +
            "d.registrationNumber as registrationNumber " +
            "FROM Doctor d " +
            "WHERE d.department.name = :departmentName")
    List<DoctorRecord> findDoctorByDepartment(@Param("departmentName") String departmentName);

    @Query("SELECT d.id as id, d.name as name, " +
            "d.specialization as specialization, " +
            "d.department.name as departmentName, " +
            "d.status as status, " +
            "d.email as email, " +
            "d.registrationNumber as registrationNumber " +
            "FROM Doctor d " +
            "WHERE d.status = :status")
    List<DoctorRecord> findDoctorByStatus(@Param("status") DoctorStatus status);

    @Query("SELECT d.id as id, d.name as name, " +
            "d.specialization as specialization, " +
            "d.department.name as departmentName, " +
            "d.status as status, " +
            "d.email as email, " +
            "d.registrationNumber as registrationNumber " +
            "FROM Doctor d")
    List<DoctorRecord> findAllDoctorDTOs(Pageable pageable);

    boolean existsByEmail(String email);

    @Query("SELECT d FROM Doctor d ORDER BY d.joiningDate DESC")
    List<Doctor> findRecentDoctors();
}
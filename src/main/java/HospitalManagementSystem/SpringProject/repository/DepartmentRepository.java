package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Department;
import HospitalManagementSystem.SpringProject.entity.Status.DepartmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);

    @Query("SELECT d FROM Department d WHERE d.Status = :status")
    List<Department> findByStatus(@Param("status") DepartmentStatus status);

    List<Department> findByHeadOfDepartment(String headOfDepartment);

    boolean existsByName(String name);
}
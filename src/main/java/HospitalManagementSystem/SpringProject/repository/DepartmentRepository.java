package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Department;
import HospitalManagementSystem.SpringProject.entity.Status.DepartmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);

    List<Department> findByStatus(DepartmentStatus status);

    List<Department> findByHeadOfDepartment(String headOfDepartment);

    boolean existsByName(String name);
}
package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.Department;
import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Status.DepartmentStatus;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    // Create
    Department addDepartment(Department department);

    // Read
    Optional<Department> getDepartmentById(Long id);

    Optional<Department> getDepartmentByName(String name);

    List<Department> getAllDepartments();

    List<Department> getActiveDepartments();

    // Update
    Department updateDepartment(Long id, Department departmentDetails);

    Department updateDepartmentStatus(Long id, DepartmentStatus status);

    Department assignHeadOfDepartment(Long id, Doctor headName);

    // Delete
    void deleteDepartment(Long id);

    // Business methods
    boolean isDepartmentExists(String name);

    long getDepartmentCount();
}

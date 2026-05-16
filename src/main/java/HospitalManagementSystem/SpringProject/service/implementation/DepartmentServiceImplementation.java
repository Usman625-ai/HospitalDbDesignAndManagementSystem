package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.Department;
import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Status.DepartmentStatus;
import HospitalManagementSystem.SpringProject.repository.DepartmentRepository;
import HospitalManagementSystem.SpringProject.service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static HospitalManagementSystem.SpringProject.entity.Status.DepartmentStatus.ACTIVE;

@RequiredArgsConstructor
@Transactional
@Service
public class DepartmentServiceImplementation implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public Department addDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("Department already exists with name: " + department.getName());
        }
        return departmentRepository.save(department);
    }

    @Override
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Optional<Department> getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public List<Department> getActiveDepartments() {
        return departmentRepository.findByStatus(ACTIVE);
    }

    @Override
    @Transactional
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        existingDepartment.setName(departmentDetails.getName());
        existingDepartment.setDescription(departmentDetails.getDescription());
        existingDepartment.setHeadOfDepartment(departmentDetails.getHeadOfDepartment());
        existingDepartment.setPhoneNumber(departmentDetails.getPhoneNumber());
        existingDepartment.setEmail(departmentDetails.getEmail());

        return departmentRepository.save(existingDepartment);
    }

    @Override
    @Transactional
    public Department updateDepartmentStatus(Long id, DepartmentStatus status) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        department.setStatus(status);
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public Department assignHeadOfDepartment(Long id, Doctor headName) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        department.setHeadOfDepartment(headName);
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public boolean isDepartmentExists(String name) {
        return departmentRepository.existsByName(name);
    }

    @Override
    public long getDepartmentCount() {
        return departmentRepository.count();
    }
}

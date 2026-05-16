package HospitalManagementSystem.SpringProject;

import HospitalManagementSystem.SpringProject.entity.Department;
import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Status.DepartmentStatus;
import HospitalManagementSystem.SpringProject.service.DepartmentService;
import HospitalManagementSystem.SpringProject.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DepartmentTest {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DoctorService doctorService;

    // ========== CREATE ==========
    @Test
    void addDepartmentTest() {
        Department department = new Department();
        department.setName("Radiology");
        department.setDescription("X-ray, MRI, CT Scan services");
        department.setFloorNumber(2);
        department.setTotalBeds(15);
        department.setAvailableBeds(10);
        department.setOpeningTime("09:00");
        department.setClosingTime("18:00");
        department.setAnnualBudget(BigDecimal.valueOf(3000000));
        department.setPhoneNumber("021-1234567");
        department.setEmail("radiology@hospital.com");

        Department saved = departmentService.addDepartment(department);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Radiology");
        System.out.println("✅ Added department: " + saved.getName() + " (ID: " + saved.getId() + ")");
    }

    // ========== READ ==========
    @Test
    void getDepartmentByIdTest() {
        long start = System.currentTimeMillis();
        Optional<Department> department = departmentService.getDepartmentById(7L);
        long time = System.currentTimeMillis() - start;

        if (department.isPresent()) {
            System.out.println("getDepartmentById(): " + time + "ms - " + department.get().getName());
        } else {
            System.out.println("getDepartmentById(): " + time + "ms - No department found with ID 1");
        }
    }

    @Test
    void getDepartmentByNameTest() {
        long start = System.currentTimeMillis();
        Optional<Department> department = departmentService.getDepartmentByName("Cardiology");
        long time = System.currentTimeMillis() - start;

        if (department.isPresent()) {
            System.out.println("getDepartmentByName(): " + time + "ms - Found: " + department.get().getName());
        } else {
            System.out.println("getDepartmentByName(): " + time + "ms - No department found");
        }
    }

    @Test
    void getAllDepartmentsTest() {
        long start = System.currentTimeMillis();
        List<Department> departments = departmentService.getAllDepartments();
        long time = System.currentTimeMillis() - start;

        System.out.println("getAllDepartments(): " + time + "ms (" + departments.size() + " records)");

        departments.forEach(dept ->
                System.out.println("  - " + dept.getName() + " (ID: " + dept.getId() + ")")
        );
        assertThat(departments).isNotEmpty();
    }

    @Test
    void getActiveDepartmentsTest() {
        long start = System.currentTimeMillis();
        List<Department> activeDepts = departmentService.getActiveDepartments();
        long time = System.currentTimeMillis() - start;

        System.out.println("getActiveDepartments(): " + time + "ms (" + activeDepts.size() + " records)");
        assertThat(activeDepts).isNotEmpty();
    }

    // ========== UPDATE ==========
    @Test
    void updateDepartmentStatusTest() {
        // First get a department that exists
        Optional<Department> existingDept = departmentService.getDepartmentById(7L);

        if (existingDept.isPresent()) {
            long start = System.currentTimeMillis();
            Department updated = departmentService.updateDepartmentStatus(7L, DepartmentStatus.CLOSED);
            long time = System.currentTimeMillis() - start;

            assertThat(updated.getStatus()).isEqualTo(DepartmentStatus.CLOSED);
            System.out.println("updateDepartmentStatus(): " + time + "ms - Department " + updated.getName() + " status changed to " + updated.getStatus());

            // Change back to ACTIVE
            departmentService.updateDepartmentStatus(7L, DepartmentStatus.ACTIVE);
        } else {
            System.out.println("⚠️ No department found with ID 1 to update status");
        }
    }

    @Test
    void assignHeadOfDepartmentTest() {
        // First check if department and doctor exist
        Optional<Department> dept = departmentService.getDepartmentById(7L);
        Optional<Doctor> doctor = doctorService.getDoctorById(32L);

        if (dept.isPresent() && doctor.isPresent()) {
            long start = System.currentTimeMillis();
            Department updated = departmentService.assignHeadOfDepartment(7L, doctor.get());
            long time = System.currentTimeMillis() - start;

            System.out.println("assignHeadOfDepartment(): " + time + "ms - Department " + updated.getName() +
                    " HOD: " + updated.getHeadOfDepartment().getName());
        } else {
            System.out.println("⚠️ Department or Doctor not found - skipping HOD assignment");
        }
    }

    // ========== PERFORMANCE TEST ==========
    @Test
    void testAllDepartmentServicesPerformance() {
        System.out.println("\n========== DEPARTMENT PERFORMANCE TEST ==========");

        // Get all departments
        long start = System.currentTimeMillis();
        List<Department> all = departmentService.getAllDepartments();
        long time1 = System.currentTimeMillis() - start;
        System.out.println("getAllDepartments(): " + time1 + "ms (" + all.size() + " records)");

        // Get active departments
        start = System.currentTimeMillis();
        List<Department> active = departmentService.getActiveDepartments();
        long time2 = System.currentTimeMillis() - start;
        System.out.println("getActiveDepartments(): " + time2 + "ms (" + active.size() + " records)");

        // Get by name
        start = System.currentTimeMillis();
        Optional<Department> byName = departmentService.getDepartmentByName("Cardiology");
        long time3 = System.currentTimeMillis() - start;
        System.out.println("getDepartmentByName(): " + time3 + "ms - " + (byName.isPresent() ? "Found" : "Not found"));

        System.out.println("✅ All department performance tests completed!");
    }
}
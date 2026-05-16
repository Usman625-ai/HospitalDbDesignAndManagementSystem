package HospitalManagementSystem.SpringProject;

import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Status.DoctorStatus;
import HospitalManagementSystem.SpringProject.record.DoctorRecord;
import HospitalManagementSystem.SpringProject.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DoctorTest {

    @Autowired
    private DoctorService doctorService;

    // ========== CREATE ==========
    @Test
    void registerDoctorTest() {
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Atif Johnson");
        doctor.setEmail("Atif.johnson@hospital.com");
        doctor.setPhoneNumber("03003234567");
        doctor.setMobileNumber("03053234568");
        doctor.setDateOfBirth(LocalDate.of(1980, 5, 15));
        doctor.setSpecialization("Cardiologist");
        doctor.setQualification("MBBS, MD, FACC");
        doctor.setExperienceYears(5);
        doctor.setConsultationFee(BigDecimal.valueOf(2500));
        doctor.setFollowUpFee(BigDecimal.valueOf(1200));
        doctor.setWorkingDays("MON,TUE,WED,THU");
        doctor.setStartTime("09:00");
        doctor.setEndTime("17:00");
        doctor.setActive(true);
        doctor.setClinicAddress("Downtown Medical Center, Floor 3");
        doctor.setChamberNumber("305");

        Doctor saved = doctorService.registerDoctor(doctor);

//        assertThat(saved.getId()).isNotNull();
//        assertThat(saved.getRegistrationNumber()).isNotNull();
        System.out.println("✅ Registered doctor: " + saved.getRegistrationNumber() + " - " + saved.getName());
    }

    // ========== READ ==========
    @Test
    void getDoctorByIdTest() {
        long start = System.currentTimeMillis();
        Optional<Doctor> doctor = doctorService.getDoctorById(32L);
        long time = System.currentTimeMillis() - start;

        if (doctor.isPresent()) {
            System.out.println("getDoctorById(): " + time + "ms - " + doctor.get().getName() + " (" + doctor.get().getSpecialization() + ")");
        } else {
            System.out.println("getDoctorById(): " + time + "ms - No doctor found with ID 1");
        }
    }

    @Test
    void getDoctorByEmailTest() {
        long start = System.currentTimeMillis();
        Optional<Doctor> doctor = doctorService.getDoctorByEmail("sarah.johnson@hospital.com");
        long time = System.currentTimeMillis() - start;

        if (doctor.isPresent()) {
            System.out.println("getDoctorByEmail(): " + time + "ms - Found: " + doctor.get().getName());
        } else {
            System.out.println("getDoctorByEmail(): " + time + "ms - No doctor found with that email");
        }
    }

    @Test
    void getAllDoctorsTest() {
        long start = System.currentTimeMillis();
        List<DoctorRecord> doctors = doctorService.getAllDoctors();
        long time = System.currentTimeMillis() - start;

        System.out.println("getAllDoctors(): " + time + "ms (" + doctors.size() + " records)");

        doctors.stream().limit(5).forEach(doc ->
                System.out.println("  - " + doc.getName() + " | " + doc.getSpecialization())
        );
        assertThat(doctors).isNotEmpty();
    }

    @Test
    void getDoctorsBySpecializationTest() {
        long start = System.currentTimeMillis();
        List<DoctorRecord> doctors = doctorService.getDoctorsBySpecialization("Cardiologist");
        long time = System.currentTimeMillis() - start;

        System.out.println("getDoctorsBySpecialization(Cardiologist): " + time + "ms (" + doctors.size() + " records)");

        if (!doctors.isEmpty()) {
            doctors.forEach(doc ->
                    System.out.println("  - " + doc.getName() + " | " + doc.getSpecialization())
            );
        }
    }

    @Test
    void getDoctorsByDepartmentTest() {
        long start = System.currentTimeMillis();
        List<DoctorRecord> doctors = doctorService.getDoctorsByDepartment("Cardiology");
        long time = System.currentTimeMillis() - start;

        System.out.println("getDoctorsByDepartment(Cardiology): " + time + "ms (" + doctors.size() + " records)");
    }

    // ========== UPDATE ==========
    @Test
    void updateDoctorTest() {
        Optional<Doctor> existingDoctor = doctorService.getDoctorById(1L);

        if (existingDoctor.isPresent()) {
            Doctor doctor = existingDoctor.get();
            String oldPhone = doctor.getPhoneNumber();
            doctor.setPhoneNumber("03111234567");
            doctor.setConsultationFee(BigDecimal.valueOf(3000));

            long start = System.currentTimeMillis();
            Doctor updated = doctorService.updateDoctor(32L, doctor);
            long time = System.currentTimeMillis() - start;

            System.out.println("updateDoctor(): " + time + "ms - Updated phone from " + oldPhone + " to " + updated.getPhoneNumber());
            assertThat(updated.getPhoneNumber()).isEqualTo("03111234567");
        } else {
            System.out.println("⚠️ No doctor found with ID 32 to update");
        }
    }

    @Test
    void updateDoctorStatusTest() {
        Optional<Doctor> existingDoctor = doctorService.getDoctorById(33L);

        if (existingDoctor.isPresent()) {
            DoctorStatus oldStatus = existingDoctor.get().getStatus();
            DoctorStatus newStatus = oldStatus == DoctorStatus.AVAILABLE ? DoctorStatus.BUSY : DoctorStatus.AVAILABLE;

            long start = System.currentTimeMillis();
            Doctor updated = doctorService.updateDoctorStatus(33L, newStatus);
            long time = System.currentTimeMillis() - start;

            System.out.println("updateDoctorStatus(): " + time + "ms - Status changed from " + oldStatus + " to " + updated.getStatus());
        } else {
            System.out.println("⚠️ No doctor found with ID 33 to update status");
        }
    }

    // ========== DELETE ==========
    @Test
    void deleteDoctorTest() {
        // First create a temporary doctor to delete
        Doctor tempDoctor = new Doctor();
        tempDoctor.setName("Temp Doctor");
        tempDoctor.setEmail("temp.doctor@delete.com");
        tempDoctor.setPhoneNumber("03009999999");
        tempDoctor.setSpecialization("Temporary");
        tempDoctor.setQualification("MBBS");
        tempDoctor.setExperienceYears(1);
        tempDoctor.setActive(true);

        Doctor saved = doctorService.registerDoctor(tempDoctor);
        Long id = saved.getId();
        System.out.println("Created temporary doctor with ID: " + id);

        long start = System.currentTimeMillis();
        doctorService.deleteDoctor(id);
        long time = System.currentTimeMillis() - start;

        Optional<Doctor> deleted = doctorService.getDoctorById(id);
        assertThat(deleted).isEmpty();
        System.out.println("deleteDoctor(): " + time + "ms - Doctor ID " + id + " deleted successfully");
    }

    // ========== BUSINESS METHODS ==========
    @Test
    void isDoctorExistsTest() {
        long start = System.currentTimeMillis();
        boolean exists = doctorService.isDoctorExists("sarah.johnson@hospital.com");
        long time = System.currentTimeMillis() - start;

        System.out.println("isDoctorExists(): " + time + "ms - " + (exists ? "Doctor exists" : "Doctor does not exist"));
    }

    @Test
    void getTotalDoctorCountTest() {
        long start = System.currentTimeMillis();
        long count = doctorService.getTotalDoctorCount();
        long time = System.currentTimeMillis() - start;

        System.out.println("getTotalDoctorCount(): " + time + "ms - " + count + " doctors");
        assertThat(count).isGreaterThan(0);
    }

    // ========== PERFORMANCE TEST ==========
    @Test
    void testAllDoctorServicesPerformance() {
        System.out.println("\n========== DOCTOR PERFORMANCE TEST ==========");

        // Get all doctors
        long start = System.currentTimeMillis();
        List<DoctorRecord> all = doctorService.getAllDoctors();
        long time1 = System.currentTimeMillis() - start;
        System.out.println("getAllDoctors(): " + time1 + "ms (" + all.size() + " records)");

        // Get by specialization
        start = System.currentTimeMillis();
        List<DoctorRecord> bySpecialization = doctorService.getDoctorsBySpecialization("Cardiologist");
        long time2 = System.currentTimeMillis() - start;
        System.out.println("getDoctorsBySpecialization(): " + time2 + "ms (" + bySpecialization.size() + " records)");

        // Get by department
        start = System.currentTimeMillis();
        List<DoctorRecord> byDepartment = doctorService.getDoctorsByDepartment("Cardiology");
        long time3 = System.currentTimeMillis() - start;
        System.out.println("getDoctorsByDepartment(): " + time3 + "ms (" + byDepartment.size() + " records)");

        // Check exists
        start = System.currentTimeMillis();
        boolean exists = doctorService.isDoctorExists("sarah.johnson@hospital.com");
        long time4 = System.currentTimeMillis() - start;
        System.out.println("isDoctorExists(): " + time4 + "ms - " + exists);

        // Total count
        start = System.currentTimeMillis();
        long count = doctorService.getTotalDoctorCount();
        long time5 = System.currentTimeMillis() - start;
        System.out.println("getTotalDoctorCount(): " + time5 + "ms (" + count + ")");

        System.out.println("✅ All doctor performance tests completed!");
    }
}

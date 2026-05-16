//package HospitalManagementSystem.SpringProject;
//
//import HospitalManagementSystem.SpringProject.entity.Medicine;
//import HospitalManagementSystem.SpringProject.entity.Prescription;
//import HospitalManagementSystem.SpringProject.entity.Status.MedicineCategory;
//import HospitalManagementSystem.SpringProject.entity.Status.MedicineStatus;
//import HospitalManagementSystem.SpringProject.entity.Status.PrescriptionStatus;
//import HospitalManagementSystem.SpringProject.record.PrescriptionRecord;
//import HospitalManagementSystem.SpringProject.repository.PrescriptionRepository;
//import HospitalManagementSystem.SpringProject.service.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class PrescriptionTest {
//
//    @Autowired
//    private PrescriptionService prescriptionService;
//
//    @Autowired
//    private PatientService patientService;
//
//    @Autowired
//    private DoctorService doctorService;
//
//    @Autowired
//    private AppointmentService appointmentService;
//
//    @Autowired
//    private MedicineService medicineService;
//
//    @Autowired
//    private PrescriptionRepository prescriptionRepository;
//    // ========== CREATE ==========
//    @Test
//    void createPrescriptionTest() {
//        Prescription prescription = new Prescription();
//        prescription.setPatient(patientService.getPatientById(54L).orElse(null));
//        prescription.setDoctor(doctorService.getDoctorById(37L).orElse(null));
//        prescription.setAppointment(appointmentService.getAppointmentById(64L).orElse(null));
//        prescription.setDiagnosis("Upper respiratory infection with fever");
//        prescription.setInvestigations("Chest X-ray, Complete blood count");
//        prescription.setGeneralAdvice("Take complete rest and stay hydrated");
//        prescription.setFollowUpRequired(true);
//        prescription.setFollowUpDate(LocalDateTime.now().plusDays(7));
//        prescription.setFollowUpDurationDays(7);
//        prescription.setValidUntil(LocalDateTime.now().plusMonths(1));
//        prescription.setPrescriptionDate(LocalDateTime.now());
//        prescription.setStatus(PrescriptionStatus.ACTIVE);
//        prescription.setPharmacyName("City Pharmacy");
//        prescription.setCreatedBy("Dr. Smith");
//
//        Prescription saved = prescriptionService.createPrescription(prescription);
//
//        assertThat(saved.getId()).isNotNull();
//        System.out.println("✅ Created prescription: " + saved.getId());
//    }
//
//    // ========== READ ==========
//    @Test
//    void getPrescriptionByIdTest() {
//        long start = System.currentTimeMillis();
//        Optional<Prescription> prescription = prescriptionService.getPrescriptionById(1L);
//        long time = System.currentTimeMillis() - start;
//
//        if (prescription.isPresent()) {
//            System.out.println("getPrescriptionById(): " + time + "ms - Prescription: " + prescription.get().getId());
//        } else {
//            System.out.println("getPrescriptionById(): " + time + "ms - No prescription found with ID 1");
//        }
//    }
//
//    @Test
//    void getAllPrescriptionsTest() {
//        long start = System.currentTimeMillis();
//        List<PrescriptionRecord> prescriptions = prescriptionService.getAllPrescriptions();
//        long time = System.currentTimeMillis() - start;
//
//        System.out.println("getAllPrescriptions(): " + time + "ms (" + prescriptions.size() + " records)");
//        assertThat(prescriptions).isNotEmpty();
//    }
//
//    @Test
//    void getPrescriptionsByPatientTest() {
//        long start = System.currentTimeMillis();
//        List<PrescriptionRecord> prescriptions = prescriptionService.getPrescriptionsByPatient(1L);
//        long time = System.currentTimeMillis() - start;
//
//        System.out.println("getPrescriptionsByPatient(): " + time + "ms (" + prescriptions.size() + " records)");
//    }
//
//    @Test
//    void getPrescriptionsByDoctorTest() {
//        long start = System.currentTimeMillis();
//        List<PrescriptionRecord> prescriptions = prescriptionService.getPrescriptionsByDoctor(34L);
//        long time = System.currentTimeMillis() - start;
//
//        System.out.println("getPrescriptionsByDoctor(): " + time + "ms (" + prescriptions.size() + " records)");
//    }
//
//    @Test
//    void getActivePrescriptionsForPatientTest() {
//        long start = System.currentTimeMillis();
//        List<Prescription> active = prescriptionService.getActivePrescriptionsForPatient(1L);
//        long time = System.currentTimeMillis() - start;
//
//        System.out.println("getActivePrescriptionsForPatient(): " + time + "ms (" + active.size() + " records)");
//    }
//
//    // ========== UPDATE ==========
//
//    @Test
//    void addMedicineToPrescriptionTest() {
//        Optional<Prescription> existing = prescriptionService.getPrescriptionById(1L);
//
//        if (existing.isPresent()) {
//            Medicine medicine = new Medicine();
//            medicine.setMedicineName("Amoxicillin");
//            medicine.setCategory(MedicineCategory.TABLET);
//            medicine.setUnitPrice(150.0);
//            medicine.setQuantity(49);
//            medicine.setReorderLevel(50);
//            medicine.setDosage("500mg");
//            medicine.setFrequency("Twice daily");
//            medicine.setDuration("7 days");
//            medicine.setStatus(MedicineStatus.ACTIVE);
//
//            Medicine savedMedicine = medicineService.addMedicine(medicine);
//
//            long start = System.currentTimeMillis();
//            Prescription updated = prescriptionService.addMedicineToPrescription(1L, savedMedicine);
//            long time = System.currentTimeMillis() - start;
//
//            System.out.println("addMedicineToPrescription(): " + time + "ms - Medicine added");
//        } else {
//            System.out.println("⚠️ No prescription found to add medicine");
//        }
//    }
//
//    @Test
//    void deactivatePrescriptionTest() {
//        Optional<Prescription> existing = prescriptionService.getPrescriptionById(1L);
//
//        if (existing.isPresent()) {
//            long start = System.currentTimeMillis();
//            Prescription deactivated = prescriptionService.deactivatePrescription(1L);
//            long time = System.currentTimeMillis() - start;
//
//            assertThat(deactivated.getStatus()).isEqualTo(PrescriptionStatus.EXPIRED);
//            System.out.println("deactivatePrescription(): " + time + "ms - Prescription deactivated");
//
//            // Reactivate for future tests
//            deactivated.setStatus(PrescriptionStatus.ACTIVE);
//            prescriptionService.updatePrescription(1L, deactivated);
//        } else {
//            System.out.println("⚠️ No prescription found to deactivate");
//        }
//    }
//
//    // ========== DELETE ==========
//    @Test
//    void deletePrescriptionTest() {
//        // First create a temporary prescription
//        Prescription temp = new Prescription();
//        temp.setPatient(patientService.getPatientById(43L).orElse(null));
//        temp.setDoctor(doctorService.getDoctorById(45L).orElse(null));
//        temp.setDiagnosis("Temporary prescription");
//        temp.setPrescriptionDate(LocalDateTime.now());
//        temp.setStatus(PrescriptionStatus.ACTIVE);
//
//        Prescription saved = prescriptionService.createPrescription(temp);
//        Long id = saved.getId();
//        System.out.println("Created temporary prescription with ID: " + id);
//
//        long start = System.currentTimeMillis();
//        prescriptionService.deletePrescription(id);
//        long time = System.currentTimeMillis() - start;
//
//        Optional<Prescription> deleted = prescriptionService.getPrescriptionById(id);
//        assertThat(deleted).isEmpty();
//        System.out.println("deletePrescription(): " + time + "ms - Prescription ID " + id + " deleted");
//    }
//
//    // ========== BUSINESS METHODS ==========
//    @Test
//    void isPrescriptionActiveTest() {
//        long start = System.currentTimeMillis();
//        boolean active = prescriptionService.isPrescriptionActive(1L);
//        long time = System.currentTimeMillis() - start;
//
//        System.out.println("isPrescriptionActive(): " + time + "ms - " + (active ? "Active" : "Inactive"));
//    }
//
//    @Test
//    void getPrescriptionCountByPatientTest() {
//        long start = System.currentTimeMillis();
//        long count = prescriptionService.getPrescriptionCountByPatient(1L);
//        long time = System.currentTimeMillis() - start;
//
//        System.out.println("getPrescriptionCountByPatient(): " + time + "ms - " + count + " prescriptions");
//    }
//
//    // ========== PERFORMANCE TEST ==========
//    @Test
//    void testAllPrescriptionServicesPerformance() {
//        System.out.println("\n========== PRESCRIPTION PERFORMANCE TEST ==========");
//
//        // Get all prescriptions
//        long start = System.currentTimeMillis();
//        List<PrescriptionRecord> all = prescriptionService.getAllPrescriptions();
//        long time1 = System.currentTimeMillis() - start;
//        System.out.println("getAllPrescriptions(): " + time1 + "ms (" + all.size() + " records)");
//
//        // Get by patient
//        start = System.currentTimeMillis();
//        List<PrescriptionRecord> byPatient = prescriptionService.getPrescriptionsByPatient(1L);
//        long time2 = System.currentTimeMillis() - start;
//        System.out.println("getPrescriptionsByPatient(): " + time2 + "ms (" + byPatient.size() + " records)");
//
//        // Get by doctor
//        start = System.currentTimeMillis();
//        List<PrescriptionRecord> byDoctor = prescriptionService.getPrescriptionsByDoctor(36L);
//        long time3 = System.currentTimeMillis() - start;
//        System.out.println("getPrescriptionsByDoctor(): " + time3 + "ms (" + byDoctor.size() + " records)");
//
//        // Get active
//        start = System.currentTimeMillis();
//        List<Prescription> active = prescriptionService.getActivePrescriptionsForPatient(1L);
//        long time4 = System.currentTimeMillis() - start;
//        System.out.println("getActivePrescriptionsForPatient(): " + time4 + "ms (" + active.size() + " records)");
//
//        // Count
//        start = System.currentTimeMillis();
//        long count = prescriptionService.getPrescriptionCountByPatient(1L);
//        long time5 = System.currentTimeMillis() - start;
//        System.out.println("getPrescriptionCountByPatient(): " + time5 + "ms (" + count + ")");
//
//        System.out.println("✅ All prescription performance tests completed!");
//    }
//}
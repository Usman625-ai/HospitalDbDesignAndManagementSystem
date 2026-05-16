//package HospitalManagementSystem.SpringProject;
//
//import HospitalManagementSystem.SpringProject.entity.Medicine;
//import HospitalManagementSystem.SpringProject.entity.Status.MedicineCategory;
//import HospitalManagementSystem.SpringProject.entity.Status.MedicineStatus;
//import HospitalManagementSystem.SpringProject.record.MedicineRecord;
//import HospitalManagementSystem.SpringProject.service.MedicineService;
//import HospitalManagementSystem.SpringProject.service.PrescriptionService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class MedicineTest {
//
//    @Autowired
//    private MedicineService medicineService;
//
//    @Autowired
//    private PrescriptionService prescriptionService;
//    // ========== CREATE ==========
//    @Test
//    void addMedicineTest() {
//        Medicine medicine = new Medicine();
//        medicine.setPrescription(prescriptionService.getPrescriptionById(6L).orElse(null));
//        medicine.setMedicineName("barufinn");
//        medicine.setCategory(MedicineCategory.TABLET);
//        medicine.setUnitPrice(50.0);
//        medicine.setQuantity(500);
//        medicine.setReorderLevel(50);
//        medicine.setDosage("500mg");
//        medicine.setFrequency("As needed");
//        medicine.setDuration("5 days");
//        medicine.setIsSubstituteAllowed(true);
//
//        Medicine saved = medicineService.addMedicine(medicine);
//
//        assertThat(saved.getId()).isNotNull();
//        assertThat(saved.getMedicineName()).isEqualTo("barufinn");
//        System.out.println("✅ Added medicine: " + saved.getMedicineName() + " (ID: " + saved.getId() + ")");
//    }
//
//    // ========== READ ==========
//    @Test
//    void getMedicineByIdTest() {
//        long start = System.currentTimeMillis();
//        Optional<Medicine> medicine = medicineService.getMedicineById(1L);
//        long time = System.currentTimeMillis() - start;
//
//        if (medicine.isPresent()) {
//            System.out.println("getMedicineById(): " + time + "ms - " + medicine.get().getMedicineName());
//        } else {
//            System.out.println("getMedicineById(): " + time + "ms - No medicine found with ID 1");
//        }
//    }
//
//    @Test
//    void getMedicineByNameTest() {
//        long start = System.currentTimeMillis();
//        List<MedicineRecord> medicine = medicineService.getMedicineByName("Paracetamol");
//        long time = System.currentTimeMillis() - start;
//
//        if (!medicine.isEmpty()) {
//            System.out.println("getMedicineByName(): " + time + "ms - Found: " + medicine.getFirst().getMedicineName());
//        } else {
//            System.out.println("getMedicineByName(): " + time + "ms - No medicine found with that name");
//        }
//    }
//
//    @Test
//    void getAllMedicinesTest() {
//        long start = System.currentTimeMillis();
//        List<MedicineRecord> medicines = medicineService.getAllMedicines();
//        long time = System.currentTimeMillis() - start;
//
//        System.out.println("getAllMedicines(): " + time + "ms (" + medicines.size() + " records)");
//        assertThat(medicines).isNotEmpty();
//    }
//
//    @Test
//    void getLowStockMedicinesTest() {
//        long start = System.currentTimeMillis();
//        List<MedicineRecord> lowStock = medicineService.getLowStockMedicines();
//        long time = System.currentTimeMillis() - start;
//
//        System.out.println("getLowStockMedicines(): " + time + "ms (" + lowStock.size() + " records)");
//
//        lowStock.forEach(m ->
//                System.out.println("  - " + m.getMedicineName() + " (Stock: " + m.getQuantity() + ", Reorder Level: " + m.getReorderLevel() + ")")
//        );
//    }
//
//    @Test
//    void searchMedicinesTest() {
//        long start = System.currentTimeMillis();
//        List<Medicine> results = medicineService.searchMedicines("para");
//        long time = System.currentTimeMillis() - start;
//
//        System.out.println("searchMedicines('para'): " + time + "ms (" + results.size() + " records)");
//
//        results.forEach(m ->
//                System.out.println("  - " + m.getMedicineName())
//        );
//    }
//
//    // ========== UPDATE ==========
//    @Test
//    void updateMedicineTest() {
//        Optional<Medicine> existing = medicineService.getMedicineById(1L);
//
//        if (existing.isPresent()) {
//            Medicine medicine = existing.get();
//            Double oldPrice = medicine.getUnitPrice();
//            medicine.setUnitPrice(55.0);
//            medicine.setCategory(MedicineCategory.CAPSULE);
//
//            long start = System.currentTimeMillis();
//            Medicine updated = medicineService.updateMedicine(1L, medicine);
//            long time = System.currentTimeMillis() - start;
//
//            System.out.println("updateMedicine(): " + time + "ms - Price changed from " + oldPrice + " to " + updated.getUnitPrice());
//        } else {
//            System.out.println("⚠️ No medicine found with ID 1 to update");
//        }
//    }
//
//    @Test
//    void updateStockTest() {
//        Optional<Medicine> existing = medicineService.getMedicineById(1L);
//
//        if (existing.isPresent()) {
//            int oldStock = existing.get().getQuantity();
//            int newStock = oldStock + 100;
//
//            long start = System.currentTimeMillis();
//            Medicine updated = medicineService.updateStock(1L, newStock);
//            long time = System.currentTimeMillis() - start;
//
//            System.out.println("updateStock(): " + time + "ms - Stock changed from " + oldStock + " to " + updated.getQuantity());
//        } else {
//            System.out.println("⚠️ No medicine found with ID 1 to update stock");
//        }
//    }
//
//    @Test
//    void reduceStockTest() {
//        Optional<Medicine> existing = medicineService.getMedicineById(1L);
//
//        if (existing.isPresent()) {
//            int oldStock = existing.get().getQuantity();
//            int reduceBy = 10;
//
//            if (oldStock >= reduceBy) {
//                long start = System.currentTimeMillis();
//                Medicine updated = medicineService.reduceStock(1L, reduceBy);
//                long time = System.currentTimeMillis() - start;
//
//                System.out.println("reduceStock(): " + time + "ms - Reduced by " + reduceBy + ", stock from " + oldStock + " to " + updated.getQuantity());
//            } else {
//                System.out.println("⚠️ Not enough stock to reduce. Current stock: " + oldStock);
//            }
//        } else {
//            System.out.println("⚠️ No medicine found with ID 1 to reduce stock");
//        }
//    }
//
//    // ========== DELETE ==========
//    @Test
//    void deleteMedicineTest() {
//        // First create a temporary medicine to delete
//        Medicine temp = new Medicine();
//        temp.setMedicineName("Temp Medicine");
//        temp.setPrescription(prescriptionService.getPrescriptionById(4L).orElse(null));
//        temp.setCategory(MedicineCategory.TABLET);
//        temp.setUnitPrice(10.0);
//        temp.setQuantity(100);
//        temp.setReorderLevel(10);
//        temp.setStatus(MedicineStatus.ACTIVE);
//
//        Medicine saved = medicineService.addMedicine(temp);
//        Long id = saved.getId();
//        System.out.println("Created temporary medicine with ID: " + id);
//
//        long start = System.currentTimeMillis();
//        medicineService.deleteMedicine(id);
//        long time = System.currentTimeMillis() - start;
//
//        Optional<Medicine> deleted = medicineService.getMedicineById(id);
//        assertThat(deleted).isEmpty();
//        System.out.println("deleteMedicine(): " + time + "ms - Medicine ID " + id + " deleted successfully");
//    }
//
//    // ========== BUSINESS METHODS ==========
//    @Test
//    void isMedicineAvailableTest() {
//        long start = System.currentTimeMillis();
//        boolean available = medicineService.isMedicineAvailable(1L, 10);
//        long time = System.currentTimeMillis() - start;
//
//        System.out.println("isMedicineAvailable(): " + time + "ms - " + (available ? "Available" : "Not enough stock"));
//    }
//
//    // ========== PERFORMANCE TEST ==========
//    @Test
//    void testAllMedicineServicesPerformance() {
//        System.out.println("\n========== MEDICINE PERFORMANCE TEST ==========");
//
//        // Get all medicines
//        long start = System.currentTimeMillis();
//        List<MedicineRecord> all = medicineService.getAllMedicines();
//        long time1 = System.currentTimeMillis() - start;
//        System.out.println("getAllMedicines(): " + time1 + "ms (" + all.size() + " records)");
//
//        // Get low stock
//        start = System.currentTimeMillis();
//        List<MedicineRecord> lowStock = medicineService.getLowStockMedicines();
//        long time2 = System.currentTimeMillis() - start;
//        System.out.println("getLowStockMedicines(): " + time2 + "ms (" + lowStock.size() + " records)");
//
//        // Search
//        start = System.currentTimeMillis();
//        List<Medicine> searched = medicineService.searchMedicines("para");
//        long time3 = System.currentTimeMillis() - start;
//        System.out.println("searchMedicines(): " + time3 + "ms (" + searched.size() + " records)");
//
//        // Check availability
//        start = System.currentTimeMillis();
//        boolean available = medicineService.isMedicineAvailable(1L, 5);
//        long time4 = System.currentTimeMillis() - start;
//        System.out.println("isMedicineAvailable(): " + time4 + "ms - " + available);
//
//        System.out.println("✅ All medicine performance tests completed!");
//    }
//}
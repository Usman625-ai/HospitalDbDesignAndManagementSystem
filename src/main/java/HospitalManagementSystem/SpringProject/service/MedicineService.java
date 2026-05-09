package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.Medicine;

import java.util.List;
import java.util.Optional;

public interface MedicineService {
    // Create
    Medicine addMedicine(Medicine medicine);

    // Read
    Optional<Medicine> getMedicineById(Long id);

    Optional<Medicine> getMedicineByName(String name);

    List<Medicine> getAllMedicines();

    List<Medicine> getLowStockMedicines();

    List<Medicine> searchMedicines(String keyword);

    // Update
    Medicine updateMedicine(Long id, Medicine medicineDetails);

    Medicine updateStock(Long id, int quantity);

    Medicine reduceStock(Long id, int quantity);

    // Delete
    void deleteMedicine(Long id);

    // Business methods
    boolean isMedicineAvailable(Long id, int requiredQuantity);
}

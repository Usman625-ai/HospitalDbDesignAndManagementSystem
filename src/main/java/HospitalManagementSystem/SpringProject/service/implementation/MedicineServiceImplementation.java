package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.Medicine;
import HospitalManagementSystem.SpringProject.record.MedicineRecord;
import HospitalManagementSystem.SpringProject.repository.MedicineRepository;
import HospitalManagementSystem.SpringProject.service.MedicineService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static HospitalManagementSystem.SpringProject.entity.Status.MedicineStatus.*;

@RequiredArgsConstructor
@Service
@Transactional
public class MedicineServiceImplementation implements MedicineService {

    private final MedicineRepository medicineRepository;

    @Override
    public Medicine addMedicine(Medicine medicine) {
        Optional<Medicine> existing = medicineRepository.findByMedicineName(medicine.getMedicineName());
        if (existing.isPresent()) {
            return existing.get();
        }

        if (medicine.getStatus() == null) {
            medicine.setStatus(ACTIVE);
        }
        return medicineRepository.save(medicine);
    }

    @Override
    public Optional<Medicine> getMedicineById(Long id) {
        return medicineRepository.findById(id);
    }

    @Override
    public Optional<Medicine> getMedicineByName(String name) {
        return medicineRepository.findByMedicineName(name);
    }

    @Override
    public List<MedicineRecord> getAllMedicines() {
        return medicineRepository.findAllMedicines();
    }

    @Override
    public List<MedicineRecord> getLowStockMedicines() {
        return medicineRepository.findLowStockMedicines();
    }

    @Override
    public List<Medicine> searchMedicines(String keyword) {
        return medicineRepository.findByMedicineNameContainingIgnoreCase(keyword);
    }

    @Override
    @Transactional
    public Medicine updateMedicine(Long id, Medicine medicineDetails) {
        Medicine existing = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        existing.setMedicineName(medicineDetails.getMedicineName());
        existing.setStatus(medicineDetails.getStatus());
        existing.setQuantity(medicineDetails.getQuantity());
        existing.setUnitPrice(medicineDetails.getUnitPrice());
        existing.setCategory(medicineDetails.getCategory());
        existing.setReorderLevel(medicineDetails.getReorderLevel());

        return medicineRepository.save(existing);
    }

    @Override
    @Transactional
    public Medicine updateStock(Long id, int quantity) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        medicine.setQuantity(quantity);

        if (quantity <= medicine.getReorderLevel()) {
            medicine.setStatus(LOW_STOCK);
        } else {
            medicine.setStatus(ACTIVE);
        }

        return medicineRepository.save(medicine);
    }

    @Override
    @Transactional
    public Medicine reduceStock(Long id, int quantity) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (medicine.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + medicine.getQuantity());
        }

        medicine.setQuantity(medicine.getQuantity() - quantity);

        if (medicine.getQuantity() <= medicine.getReorderLevel()) {
            medicine.setStatus(LOW_STOCK);
        }

        return medicineRepository.save(medicine);
    }

    @Override
    @Transactional
    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }

    @Override
    public boolean isMedicineAvailable(Long id, int requiredQuantity) {
        Medicine medicine = medicineRepository.findById(id).orElse(null);
        return medicine != null && medicine.getQuantity() >= requiredQuantity;
    }
}
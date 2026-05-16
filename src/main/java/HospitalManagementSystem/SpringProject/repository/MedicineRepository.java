package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Medicine;
import HospitalManagementSystem.SpringProject.entity.Status.MedicineStatus;
import HospitalManagementSystem.SpringProject.record.MedicineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // Return Optional<Medicine> - full entity
    Optional<Medicine> findByMedicineName(String name);

    // Return List<Medicine> - full entities
    List<Medicine> findByQuantityLessThan(int quantity);

    List<Medicine> findByMedicineNameContainingIgnoreCase(String keyword);

    // Return List<MedicineRecord> - DTO projection
    @Query("SELECT m.medicineName, m.status, m.quantity, m.reorderLevel, m.unitPrice FROM Medicine m")
    List<MedicineRecord> findAllMedicines();

    @Query("SELECT m.medicineName, m.status, m.quantity, m.reorderLevel, m.unitPrice FROM Medicine m WHERE m.status = :status")
    List<MedicineRecord> findByStatus(@Param("status") MedicineStatus status);

    @Query("SELECT m.medicineName, m.status, m.quantity, m.reorderLevel, m.unitPrice FROM Medicine m WHERE m.quantity <= m.reorderLevel")
    List<MedicineRecord> findLowStockMedicines();

    boolean existsByMedicineName(String medicineName);
}
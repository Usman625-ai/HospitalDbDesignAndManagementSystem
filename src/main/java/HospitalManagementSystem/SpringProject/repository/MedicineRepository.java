package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Medicine;
import HospitalManagementSystem.SpringProject.entity.Status.MedicineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Optional<Medicine> findBymedicineName(String name);

    List<Medicine> findByquantityLessThan(int threshold);

    List<Medicine> findByStatus(MedicineStatus status);

    @Query("SELECT m FROM Medicine m WHERE m.quantity <=  m.reorderLevel")
    List<Medicine> findLowStockMedicines();

    List<Medicine> findBymedicineNameContainingIgnoreCase(String name);
}
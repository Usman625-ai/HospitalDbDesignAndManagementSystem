package HospitalManagementSystem.SpringProject.record;

public interface MedicineRecord {
    String getMedicineName();
    String getStatus();
    Integer getQuantity();
    Integer getReorderLevel();
    Double getUnitPrice();
}
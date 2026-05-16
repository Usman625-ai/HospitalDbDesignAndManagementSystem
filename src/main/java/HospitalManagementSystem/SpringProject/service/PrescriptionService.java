package HospitalManagementSystem.SpringProject.service;


import HospitalManagementSystem.SpringProject.entity.Prescription;
import HospitalManagementSystem.SpringProject.entity.Medicine;
import HospitalManagementSystem.SpringProject.record.PrescriptionRecord;

import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    // Create
    Prescription createPrescription(Prescription prescription);

    // Read
    Optional<Prescription> getPrescriptionById(Long id);

    List<PrescriptionRecord> getAllPrescriptions();

    List<PrescriptionRecord> getPrescriptionsByPatient(Long patientId);

    List<PrescriptionRecord> getPrescriptionsByDoctor(Long doctorId);

    List<Prescription> getActivePrescriptionsForPatient(Long patientId);

    // Update
    Prescription updatePrescription(Long id, Prescription prescriptionDetails);

    Prescription addMedicineToPrescription(Long id, Long medicineId, String dosage, String frequency, String duration, Integer quantity);

    Prescription deactivatePrescription(Long id);

    // Delete
    void deletePrescription(Long id);

    // Business methods
    boolean isPrescriptionActive(Long id);

    long getPrescriptionCountByPatient(Long patientId);
}

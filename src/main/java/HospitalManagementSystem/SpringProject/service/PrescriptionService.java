package HospitalManagementSystem.SpringProject.service;


import HospitalManagementSystem.SpringProject.entity.Prescription;
import HospitalManagementSystem.SpringProject.entity.Medicine;

import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    // Create
    Prescription createPrescription(Prescription prescription);

    // Read
    Optional<Prescription> getPrescriptionById(Long id);

    List<Prescription> getAllPrescriptions();

    List<Prescription> getPrescriptionsByPatient(Long patientId);

    List<Prescription> getPrescriptionsByDoctor(Long doctorId);

    List<Prescription> getActivePrescriptionsForPatient(Long patientId);

    // Update
    Prescription updatePrescription(Long id, Prescription prescriptionDetails);

    Prescription addMedicineToPrescription(Long id, Medicine medicine);

    Prescription deactivatePrescription(Long id);

    // Delete
    void deletePrescription(Long id);

    // Business methods
    boolean isPrescriptionActive(Long id);

    long getPrescriptionCountByPatient(Long patientId);
}

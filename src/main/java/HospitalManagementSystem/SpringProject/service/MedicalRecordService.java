package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.MedicalRecord;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordService {
    // Create
    MedicalRecord addMedicalRecord(MedicalRecord medicalRecord);

    // Read
    Optional<MedicalRecord> getMedicalRecordById(Long id);

    List<MedicalRecord> getAllMedicalRecords();

    List<MedicalRecord> getMedicalRecordsByPatient(Long patientId);

    List<MedicalRecord> getPatientMedicalHistory(Long patientId);

    // Update
    MedicalRecord updateMedicalRecord(Long id, MedicalRecord recordDetails);

    // Delete
    void deleteMedicalRecord(Long id);

}

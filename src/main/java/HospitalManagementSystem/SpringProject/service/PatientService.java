package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.PatientStatus;
import HospitalManagementSystem.SpringProject.record.PatientRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    // Create
    Patient registerPatient(Patient patient);

    List<Patient> registerPatients(List<Patient> patients);

    // Read
    Optional<Patient> getPatientById(Long id);

    Optional<Patient> getPatientByEmail(String email);

    List<PatientRecord> getAllPatients();

    List<PatientRecord> getPatientsByStatus(PatientStatus status);

    List<PatientRecord> searchPatientsByLastName(String name);

    // Update
    Patient updatePatient(Long id, Patient patientDetails);

    Patient updatePatientStatus(Long id, PatientStatus status);

    // Delete
    void deletePatient(Long id);

    // Business methods
    boolean isPatientExists(String email);

    long getTotalPatientCount();

    Page<PatientRecord> get10RecentlyRegisteredPatients(Pageable pageable);
}

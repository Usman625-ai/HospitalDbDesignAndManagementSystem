package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.PatientStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    // Create
    Patient registerPatient(Patient patient);

    // Read
    Optional<Patient> getPatientById(Long id);

    Optional<Patient> getPatientByEmail(String email);

    List<Patient> getAllPatients();

    List<Patient> getPatientsByStatus(PatientStatus status);

    List<Patient> searchPatientsByLastName(String name);

    // Update
    Patient updatePatient(Long id, Patient patientDetails);

    Patient updatePatientStatus(Long id, PatientStatus status);

    // Delete
    void deletePatient(Long id);

    // Business methods
    boolean isPatientExists(String email);

    long getTotalPatientCount();

    List<Patient> get10RecentlyRegisteredPatients();
}

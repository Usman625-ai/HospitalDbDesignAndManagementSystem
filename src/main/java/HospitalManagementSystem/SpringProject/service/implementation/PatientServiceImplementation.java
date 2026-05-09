package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.PatientStatus;
import HospitalManagementSystem.SpringProject.repository.PatientRepository;
import HospitalManagementSystem.SpringProject.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static HospitalManagementSystem.SpringProject.entity.Status.PatientStatus.ACTIVE;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImplementation implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public Patient registerPatient(Patient patient) {
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new RuntimeException("Patient already exists with email: " + patient.getEmail());
        }

        patient.setStatus(ACTIVE);
        patient.setRegisteredAt(LocalDateTime.now());

        return patientRepository.save(patient);
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public List<Patient> getPatientsByStatus(PatientStatus status) {
        return patientRepository.findByStatus(status);
    }

    @Override
    public List<Patient> searchPatientsByLastName(String name) {
        return patientRepository.findByLastNameContaining(name);
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        existingPatient.setFirstName(patientDetails.getFirstName());
        existingPatient.setLastName(patientDetails.getLastName());
        existingPatient.setPhoneNumber(patientDetails.getPhoneNumber());
        existingPatient.setAddress(patientDetails.getAddress());
        existingPatient.setEmergencyContact(patientDetails.getEmergencyContact());
        existingPatient.setMedicalRecords(patientDetails.getMedicalRecords());
        existingPatient.setUpdatedAt(LocalDateTime.now());

        return patientRepository.save(existingPatient);
    }

    @Override
    public Patient updatePatientStatus(Long id, PatientStatus status) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        patient.setStatus(status);
        patient.setUpdatedAt(LocalDateTime.now());

        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    @Override
    public boolean isPatientExists(String email) {

        return patientRepository.existsByEmail(email);
    }

    @Override
    public long getTotalPatientCount() {

        return patientRepository.count();
    }

    @Override
    public List<Patient> get10RecentlyRegisteredPatients() {
//        return patientRepository.findAll().stream()
//                .limit(10)
//                .toList(); not efficient,it will load all the data

        PageRequest pagereq = PageRequest.of(0,10);
        return patientRepository.findRecentPatients(pagereq).getContent();
//        getContent() will return list
    }
}

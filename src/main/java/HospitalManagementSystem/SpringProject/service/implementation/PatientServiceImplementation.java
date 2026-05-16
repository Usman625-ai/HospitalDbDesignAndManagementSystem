package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.PatientStatus;
import HospitalManagementSystem.SpringProject.record.PatientRecord;
import HospitalManagementSystem.SpringProject.repository.PatientRepository;
import HospitalManagementSystem.SpringProject.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static HospitalManagementSystem.SpringProject.entity.Status.PatientStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class PatientServiceImplementation implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public Patient registerPatient(Patient patient) {
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new RuntimeException("Patient already exists with email: " + patient.getEmail());
        }
//        int lastNum = 0;
//        Patient lastPatient = patientRepository.findTopByOrderByIdDesc();
//        if (lastPatient != null && lastPatient.getPatientId() != null) {
//            String lastId = lastPatient.getPatientId();
//            lastNum = Integer.parseInt(lastId.substring(7));
//            lastNum = lastNum + 1;
//        }
//        int year = java.time.Year.now().getValue();
//        patient.setPatientId(String.format("PAT%d%05d", year, lastNum));
        Patient saved = patientRepository.save(patient);// ← This reloads the patient_id from database
        return patientRepository.findById(saved.getId()).orElse(saved);
    }

    @Transactional
    @Override
    public List<Patient> registerPatients(List<Patient> patients) {
        // Use saveAll for batching
        return patientRepository.saveAll(patients);
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
    public List<PatientRecord> getAllPatients() {
        return patientRepository.findAllPatients();
    }

    @Override
    public List<PatientRecord> getPatientsByStatus(PatientStatus status) {
        return patientRepository.findByStatus(status);
    }

    @Override
    public List<PatientRecord> searchPatientsByLastName(String name) {
        return patientRepository.findByLastNameContaining(name);
    }

    @Override
    @Transactional
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        existingPatient.setFirstName(patientDetails.getFirstName());
        existingPatient.setLastName(patientDetails.getLastName());
        existingPatient.setPhoneNumber(patientDetails.getPhoneNumber());
        existingPatient.setEmail(patientDetails.getEmail());
        existingPatient.setUpdatedAt(LocalDateTime.now());

        return patientRepository.save(existingPatient);
    }

    @Override
    @Transactional
    public Patient updatePatientStatus(Long id, PatientStatus status) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        patient.setStatus(status);
        patient.setUpdatedAt(LocalDateTime.now());

        return patientRepository.save(patient);
    }

    @Override
    @Transactional
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
    public Page<PatientRecord> get10RecentlyRegisteredPatients(Pageable pageable) {
        PageRequest pagereq = PageRequest.of(0,10);
        return patientRepository.findRecentPatients(pagereq);
    }
}

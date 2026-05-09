package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Investigation;
import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.InvestigationStatus;
import HospitalManagementSystem.SpringProject.repository.DoctorRepository;
import HospitalManagementSystem.SpringProject.repository.InvestigationRepository;
import HospitalManagementSystem.SpringProject.repository.PatientRepository;
import HospitalManagementSystem.SpringProject.service.InvestigationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static HospitalManagementSystem.SpringProject.entity.Status.InvestigationStatus.COMPLETED;
import static HospitalManagementSystem.SpringProject.entity.Status.InvestigationStatus.PENDING;

@Transactional
@RequiredArgsConstructor
@Service
public class InvestigationServiceImplementation implements InvestigationService {
    private final InvestigationRepository investigationRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    @Override
    public Investigation requestInvestigation(Investigation investigation) {
        Patient patient = patientRepository.findById(investigation.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(investigation.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        investigation.setPatient(patient);
        investigation.setDoctor(doctor);
        investigation.setRequestedDate(LocalDateTime.now());
        investigation.setStatus(PENDING);

        return investigationRepository.save(investigation);
    }

    @Override
    public Optional<Investigation> getInvestigationById(Long id) {
        return investigationRepository.findById(id);
    }

    @Override
    public List<Investigation> getAllInvestigations() {
        return investigationRepository.findAll();
    }

    @Override
    public List<Investigation> getInvestigationsByPatient(Long patientId) {
        return investigationRepository.findByPatientIdOrderByRequestedDateDesc(patientId);
    }

    @Override
    public List<Investigation> getPendingInvestigations(Long patientId) {
        return investigationRepository.findPendingInvestigationsByPatient(patientId);
    }

    @Override
    public Investigation updateInvestigation(Long id, Investigation investigationDetails) {
        Investigation existing = investigationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investigation not found"));
        existing.setInvestigationType(investigationDetails.getInvestigationType());
        existing.setDescription(investigationDetails.getDescription());
        existing.setUrgency(investigationDetails.getUrgency());
        return investigationRepository.save(existing);
    }

    @Override
    public Investigation addResults(Long id, String results, String reportUrl) {
        Investigation investigation = investigationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investigation not found"));

        investigation.setResults(results);
        investigation.setReportUrl(reportUrl);
        investigation.setResultDate(LocalDateTime.now());
        investigation.setStatus(COMPLETED);

        return investigationRepository.save(investigation);
    }

    @Override
    public Investigation updateStatus(Long id, InvestigationStatus status) {
        Investigation investigation = investigationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investigation not found"));

        investigation.setStatus(status);
        return investigationRepository.save(investigation);
    }

    @Override
    public void deleteInvestigation(Long id) {
        investigationRepository.deleteById(id);
    }

    @Override
    public long getPendingCount() {
        return investigationRepository.findByStatus(PENDING).size();
    }
}

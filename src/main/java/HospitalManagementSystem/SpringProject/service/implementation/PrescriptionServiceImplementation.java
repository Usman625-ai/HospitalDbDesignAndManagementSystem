package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.*;
import HospitalManagementSystem.SpringProject.repository.AppointmentRepository;
import HospitalManagementSystem.SpringProject.repository.DoctorRepository;
import HospitalManagementSystem.SpringProject.repository.PatientRepository;
import HospitalManagementSystem.SpringProject.repository.PrescriptionRepository;
import HospitalManagementSystem.SpringProject.service.PrescriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static HospitalManagementSystem.SpringProject.entity.Status.PrescriptionStatus.ACTIVE;
import static HospitalManagementSystem.SpringProject.entity.Status.PrescriptionStatus.DISPENSED;

@Transactional
@Service
@RequiredArgsConstructor
public class PrescriptionServiceImplementation implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public Prescription createPrescription(Prescription prescription) {
        // Validate patient exists
        Patient patient = patientRepository.findById(prescription.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(prescription.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Validate appointment if provided
        if (prescription.getAppointment() != null && prescription.getAppointment().getId() != null) {
            Appointment appointment = appointmentRepository.findById(prescription.getAppointment().getId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            prescription.setAppointment(appointment);
        }

        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setPrescriptionDate(LocalDateTime.now());
        prescription.setStatus(ACTIVE);

        return prescriptionRepository.save(prescription);
    }

    @Override
    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    @Override
    public List<Prescription> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    @Override
    public List<Prescription> getPrescriptionsByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Prescription> getActivePrescriptionsForPatient(Long patientId) {
        return prescriptionRepository.findActivePrescriptionsByPatient(patientId);
    }

    @Override
    public Prescription updatePrescription(Long id, Prescription prescriptionDetails) {
        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));

        existingPrescription.setDiagnosis(prescriptionDetails.getDiagnosis());
        existingPrescription.setMedicines(prescriptionDetails.getMedicines());
        existingPrescription.setFollowUpInstructions(prescriptionDetails.getFollowUpInstructions());
        existingPrescription.setFollowUpDate(prescriptionDetails.getFollowUpDate());

        return prescriptionRepository.save(existingPrescription);
    }

    @Override
    public Prescription addMedicineToPrescription(Long id, Medicine medicine) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));

        List<Medicine> currentMedicines = prescription.getMedicines();
        currentMedicines.add(medicine);
        return prescriptionRepository.save(prescription);
    }

    @Override
    public Prescription deactivatePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));
        prescription.setStatus(DISPENSED);
        return prescriptionRepository.save(prescription);
    }

    @Override
    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new RuntimeException("Prescription not found with id: " + id);
        }
        prescriptionRepository.deleteById(id);
    }

    @Override
    public boolean isPrescriptionActive(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));
        if(ACTIVE.equals(prescription.getStatus())){
            return true;
        }
        return false;
    }

    @Override
    public long getPrescriptionCountByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId).size();
    }
}

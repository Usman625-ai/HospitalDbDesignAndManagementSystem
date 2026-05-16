package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.*;
import HospitalManagementSystem.SpringProject.record.PrescriptionRecord;
import HospitalManagementSystem.SpringProject.repository.*;
import HospitalManagementSystem.SpringProject.service.PrescriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final MedicineRepository medicineRepository;
    private final PrescriptionMedicineRepository prescriptionMedicineRepository;

    @Override
    public Prescription createPrescription(Prescription prescription) {
        Patient patient = patientRepository.findById(prescription.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(prescription.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (prescription.getAppointment() != null && prescription.getAppointment().getId() != null) {
            Appointment appointment = appointmentRepository.findById(prescription.getAppointment().getId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            prescription.setAppointment(appointment);
        }

        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setStatus(ACTIVE);
        prescription.setPrescriptionDate(LocalDateTime.now());

        return prescriptionRepository.save(prescription);
    }

    @Override
    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Override
    public List<PrescriptionRecord> getAllPrescriptions() {
        return prescriptionRepository.findAllPrescriptions();
    }

    @Override
    public List<PrescriptionRecord> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    @Override
    public List<PrescriptionRecord> getPrescriptionsByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Prescription> getActivePrescriptionsForPatient(Long patientId) {
        return prescriptionRepository.findActivePrescriptionsByPatient(patientId);
    }

    @Override
    @Transactional
    public Prescription updatePrescription(Long id, Prescription prescriptionDetails) {
        Prescription existing = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        existing.setDiagnosis(prescriptionDetails.getDiagnosis());
        existing.setInvestigations(prescriptionDetails.getInvestigations());
        existing.setGeneralAdvice(prescriptionDetails.getGeneralAdvice());
        existing.setValidUntil(prescriptionDetails.getValidUntil());

        return prescriptionRepository.save(existing);
    }

    @Override
    @Transactional
    public Prescription addMedicineToPrescription(Long id, Long medicineId, String dosage, String frequency, String duration, Integer quantity) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        PrescriptionMedicine pm = new PrescriptionMedicine();
        pm.setPrescription(prescription);
        pm.setMedicine(medicine);
        pm.setDosage(dosage);
        pm.setFrequency(frequency);
        pm.setDuration(duration);
        pm.setQuantity(quantity);
        pm.setIsSubstituteAllowed(true);
        pm.setPrescribedAt(LocalDateTime.now());

        prescriptionMedicineRepository.save(pm);

        // Add to prescription's list
        if (prescription.getPrescriptionMedicines() == null) {
            prescription.setPrescriptionMedicines(new ArrayList<>());
        }
        prescription.getPrescriptionMedicines().add(pm);

        return prescriptionRepository.save(prescription);
    }

    @Override
    @Transactional
    public Prescription deactivatePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        prescription.setStatus(DISPENSED);
        return prescriptionRepository.save(prescription);
    }

    @Override
    @Transactional
    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new RuntimeException("Prescription not found with id: " + id);
        }
        prescriptionRepository.deleteById(id);
    }

    @Override
    public boolean isPrescriptionActive(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        return ACTIVE.equals(prescription.getStatus());
    }

    @Override
    public long getPrescriptionCountByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId).size();
    }
}
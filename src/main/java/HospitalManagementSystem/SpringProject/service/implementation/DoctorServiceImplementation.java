package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Status.DoctorStatus;
import HospitalManagementSystem.SpringProject.record.DoctorRecord;
import HospitalManagementSystem.SpringProject.repository.DoctorRepository;
import HospitalManagementSystem.SpringProject.service.DoctorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static HospitalManagementSystem.SpringProject.entity.Status.DoctorStatus.AVAILABLE;


@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImplementation implements DoctorService {
    private final DoctorRepository doctorRepository;

    @Override
    public Doctor registerDoctor(Doctor doctor) {
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new RuntimeException("Doctor already exists with ID: " + doctor.getEmail());
        }
//        int lastNum = 0;
//        String maxDoctorid = doctorRepository.findMaxDoctorId();
//        if (maxDoctorid != null) {
//            lastNum = Integer.parseInt(maxDoctorid.substring(7));
//            lastNum = lastNum + 1;
//        }
        doctor.setJoiningDate(LocalDateTime.now());
        return doctorRepository.save(doctor);
//        entityManager.refresh(saved);  // ← This reloads the patient_id from database
    }

    @Override
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Optional<Doctor> getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Override
    public List<DoctorRecord> getAllDoctors() {
        PageRequest page = PageRequest.of(0, 10);
        return doctorRepository.findAllDoctorDTOs(page);
    }

    @Override
    public List<DoctorRecord> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findDoctorBySpecialization(specialization);
    }

    @Override
    public List<DoctorRecord> getDoctorsByDepartment(String department) {
        return doctorRepository.findDoctorByDepartment(department);
    }

    @Override
    @Transactional
    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        existingDoctor.setName(doctorDetails.getName());
        existingDoctor.setPhoneNumber(doctorDetails.getPhoneNumber());
        existingDoctor.setSpecialization(doctorDetails.getSpecialization());
        existingDoctor.setDepartment(doctorDetails.getDepartment());
        existingDoctor.setQualification(doctorDetails.getQualification());
        existingDoctor.setExperienceYears(doctorDetails.getExperienceYears());
        existingDoctor.setConsultationFee(doctorDetails.getConsultationFee());
        existingDoctor.setStartTime(doctorDetails.getStartTime());

        return doctorRepository.save(existingDoctor);
    }

    @Override
    @Transactional
    public Doctor updateDoctorStatus(Long id, DoctorStatus status) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        doctor.setStatus(status);
        return doctorRepository.save(doctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    @Override
    public boolean isDoctorExists(String email) {
        return doctorRepository.existsByEmail(email);
    }

    @Override
    public long getTotalDoctorCount() {
        return doctorRepository.count();
    }
}

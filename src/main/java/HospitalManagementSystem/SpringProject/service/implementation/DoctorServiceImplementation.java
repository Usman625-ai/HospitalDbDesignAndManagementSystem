package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Status.DoctorStatus;
import HospitalManagementSystem.SpringProject.repository.DoctorRepository;
import HospitalManagementSystem.SpringProject.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
            throw new RuntimeException("Doctor already exists with email: " + doctor.getEmail());
        }

        doctor.setStatus(AVAILABLE);
        doctor.setJoiningDate(LocalDateTime.now());

        return doctorRepository.save(doctor);
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
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    @Override
    public List<Doctor> getDoctorsByDepartment(String department) {
        return doctorRepository.findByDepartment(department);
    }

    @Override
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

package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Status.DoctorStatus;

import java.util.List;
import java.util.Optional;

public interface DoctorService {

    // Create
    Doctor registerDoctor(Doctor doctor);

    // Read
    Optional<Doctor> getDoctorById(Long id);

    Optional<Doctor> getDoctorByEmail(String email);

    List<Doctor> getAllDoctors();

    List<Doctor> getDoctorsBySpecialization(String specialization);

    List<Doctor> getDoctorsByDepartment(String department);

    // Update
    Doctor updateDoctor(Long id, Doctor doctorDetails);

    Doctor updateDoctorStatus(Long id, DoctorStatus status);

    // Delete
    void deleteDoctor(Long id);

    // Business methods
    boolean isDoctorExists(String email);

    long getTotalDoctorCount();

}

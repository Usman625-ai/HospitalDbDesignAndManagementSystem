package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.Doctor;
import HospitalManagementSystem.SpringProject.entity.Status.DoctorStatus;
import HospitalManagementSystem.SpringProject.record.DoctorRecord;

import java.util.List;
import java.util.Optional;

public interface DoctorService {

    // Create
    Doctor registerDoctor(Doctor doctor);

    // Read
    Optional<Doctor> getDoctorById(Long id);

    Optional<Doctor> getDoctorByEmail(String email);

    List<DoctorRecord> getAllDoctors();

    List<DoctorRecord> getDoctorsBySpecialization(String specialization);

    List<DoctorRecord> getDoctorsByDepartment(String departmentname);

    // Update
    Doctor updateDoctor(Long id, Doctor doctorDetails);

    Doctor updateDoctorStatus(Long id, DoctorStatus status);

    // Delete
    void deleteDoctor(Long id);

    // Business methods
    boolean isDoctorExists(String email);

    long getTotalDoctorCount();

}

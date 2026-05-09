package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByStatus(String status);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findByappointmentDateTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

//    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDateTime = :date")
    List<Appointment> findByDoctorIdAndappointmentDateTime(@Param("doctorId") Long doctorId, @Param("date") LocalDateTime date);

//    boolean existsByDoctorIdAndappointmentDateTimeAndStatusNot(Long doctorId, LocalDateTime date, String status);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND DATE(a.appointmentDateTime) = :date")
    List<Appointment> findDoctorAppointmentsByDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

//    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId AND DATE(a.appointmentDateTime) = :date")
//    long countAppointmentsByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
}
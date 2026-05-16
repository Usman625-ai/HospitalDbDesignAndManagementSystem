package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Appointment;
import HospitalManagementSystem.SpringProject.entity.Status.AppointmentStatus;
import HospitalManagementSystem.SpringProject.record.AppointmentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // In AppointmentRepository
    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.patient " +
            "JOIN FETCH a.doctor " +
            "WHERE a.id = :id")
    Optional<Appointment> findByIdWithDetails(@Param("id") Long id);

    Page<Appointment> findByPatientId(Long patientId, Pageable page);

    @Query(value = """

            SELECT 
            a.id as id,
            a.appointment_date_time as appointmentDateTime,
            a.status as status,
            p.first_name as patientFirstName,
            p.last_name as patientLastName,
            d.name as doctorName
        FROM appointment a
        JOIN patient p ON p.id = a.patient_id
        JOIN doctor d ON d.id = a.doctor_id
        WHERE a.doctor_id = :doctorId
        """, nativeQuery = true)
    List<AppointmentRecord> findAppointmentRecordsByDoctor(@Param("doctorId") Long doctorId);

    Page<Appointment> findByStatus(AppointmentStatus status, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findByappointmentDateTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDateTime = :date")
    List<Appointment> findByDoctorIdAndappointmentDateTime(@Param("doctorId") Long doctorId, @Param("date") LocalDateTime date);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND DATE(a.appointmentDateTime) = :date")
    List<Appointment> findDoctorAppointmentsByDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
}
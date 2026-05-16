package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Bill;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentStatus;
import HospitalManagementSystem.SpringProject.record.BillRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    // Add DTO methods for read operations
    @Query("SELECT b.id as id, b.totalAmount as totalAmount, " +
            "b.paymentStatus as paymentStatus, b.billDate as billDate, " +
            "CONCAT(p.firstName, ' ', p.lastName) as patientName, " +
            "d.name as doctorName, " +
            "CAST(b.appointment.id AS string) as appointmentId " +
            "FROM Bill b " +
            "JOIN b.patient p " +
            "JOIN b.doctor d")
    List<BillRecord> findAllBillsSummary(Pageable pageable);

    @Query("SELECT b.id as id, b.totalAmount as totalAmount, " +
            "b.paymentStatus as paymentStatus, b.billDate as billDate, " +
            "CONCAT(p.firstName, ' ', p.lastName) as patientName, " +
            "d.name as doctorName, " +
            "CAST(b.appointment.id AS string) as appointmentId " +
            "FROM Bill b " +
            "JOIN b.patient p " +
            "JOIN b.doctor d " +
            "WHERE b.patient.id = :patientId")
    List<BillRecord> findBillsByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT b.id as id, b.totalAmount as totalAmount, " +
            "b.paymentStatus as paymentStatus, b.billDate as billDate, " +
            "CONCAT(p.firstName, ' ', p.lastName) as patientName, " +
            "d.name as doctorName, " +
            "CAST(b.appointment.id AS string) as appointmentId " +
            "FROM Bill b " +
            "JOIN b.patient p " +
            "JOIN b.doctor d " +
            "WHERE b.patient.id = :patientId and b.paymentStatus = :paymentStatus")
    List<BillRecord> findByPatientIdAndPaymentStatus(@Param("patientId") Long patientId,@Param("paymentStatus") PaymentStatus paymentStatus);

    @Query("SELECT b.id as id, b.totalAmount as totalAmount, " +
            "b.paymentStatus as paymentStatus, b.billDate as billDate, " +
            "CONCAT(p.firstName, ' ', p.lastName) as patientName, " +
            "d.name as doctorName, " +
            "CAST(b.appointment.id AS string) as appointmentId " +
            "FROM Bill b " +
            "JOIN b.patient p " +
            "JOIN b.doctor d " +
            "WHERE b.paymentStatus = :paymentStatus")
    List<BillRecord> findBillsByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus);

    @Query("SELECT b.id as id, b.totalAmount as totalAmount, " +
            "b.paymentStatus as paymentStatus, b.billDate as billDate, " +
            "CONCAT(p.firstName, ' ', p.lastName) as patientName, " +
            "p.patientId as patientId " +
            "FROM Bill b " +
            "JOIN b.patient p " +
            "JOIN b.doctor d " +
            "WHERE DATE(b.billDate) = CURRENT_DATE")
    List<BillRecord> findTodayBills();

    @Query("SELECT b.id as id, b.totalAmount as totalAmount, " +
            "b.paymentStatus as paymentStatus, b.billDate as billDate, " +
            "CONCAT(p.firstName, ' ', p.lastName) as patientName, " +
            "p.patientId as patientId " +
            "FROM Bill b " +
            "JOIN b.patient p " +
            "JOIN b.doctor d " +
            "WHERE b.dueDate < CURRENT_TIMESTAMP AND b.paymentStatus = PENDING")
    List<BillRecord> findOverdueBills();

    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.paymentStatus = PAID AND b.billDate BETWEEN :start AND :end")
    Double getTotalRevenueBetweenDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.paymentStatus = PENDING")
    long countPendingPayments();

}
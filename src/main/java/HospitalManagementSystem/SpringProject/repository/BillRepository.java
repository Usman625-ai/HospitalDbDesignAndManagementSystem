package HospitalManagementSystem.SpringProject.repository;

import HospitalManagementSystem.SpringProject.entity.Bill;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPatientId(Long patientId);

    List<Bill> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Bill> findByBillDateBetween(LocalDateTime start, LocalDateTime end);

    List<Bill> findByPatientIdAndPaymentStatus(Long patientId, PaymentStatus paymentStatus);

    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.paymentStatus = PAID AND b.billDate BETWEEN :start AND :end")
    Double getTotalRevenueBetweenDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.paymentStatus = PENDING")
    long countPendingPayments();

    @Query("SELECT b FROM Bill b WHERE b.dueDate < CURRENT_TIMESTAMP AND b.paymentStatus = PENDING")
    List<Bill> findOverdueBills();
}
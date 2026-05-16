package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.Bill;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentMethod;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentStatus;
import HospitalManagementSystem.SpringProject.record.BillRecord;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BillService {
    // Create
    Bill generateBill(Bill bill);

    // Read
    Optional<Bill> getBillById(Long id);

    // Read operations - Use DTO
    List<BillRecord> getBillsByPatient(Long patientId);
    List<BillRecord> getBillsByPaymentStatus(PaymentStatus paymentStatus);
    List<BillRecord> getTodayBills();
    List<BillRecord> getOverdueBills();

    // Keep these if needed for specific use cases
    List<BillRecord> getAllBills();

    // Update
    Bill updateBill(Long id, Bill billDetails);

    Bill processPayment(Long id, PaymentMethod paymentMethod);

    Bill markAsPaid(Long id);

    // Delete
    void deleteBill(Long id);

    // Business methods
    double getTotalRevenueForToday();

    double getTotalRevenueForMonth();

    long getPendingPaymentsCount();
}

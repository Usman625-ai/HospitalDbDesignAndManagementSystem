package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.Bill;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentMethod;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentStatus;

import java.util.List;
import java.util.Optional;

public interface BillService {
    // Create
    Bill generateBill(Bill bill);

    // Read
    Optional<Bill> getBillById(Long id);

    List<Bill> getAllBills();

    List<Bill> getBillsByPatient(Long patientId);

    List<Bill> getBillsByPaymentStatus(PaymentStatus paymentStatus);

    List<Bill> getTodayBills();

    List<Bill> getOverdueBills();

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

    double getPatientOutstandingBalance(Long patientId);
}

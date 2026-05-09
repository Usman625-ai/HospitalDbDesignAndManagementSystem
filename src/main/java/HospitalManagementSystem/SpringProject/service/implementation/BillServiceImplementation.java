package HospitalManagementSystem.SpringProject.service.implementation;

import HospitalManagementSystem.SpringProject.entity.Bill;
import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentMethod;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentStatus;
import HospitalManagementSystem.SpringProject.repository.BillRepository;
import HospitalManagementSystem.SpringProject.repository.PatientRepository;
import HospitalManagementSystem.SpringProject.service.BillService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static HospitalManagementSystem.SpringProject.entity.Status.PaymentMethod.CASH;
import static HospitalManagementSystem.SpringProject.entity.Status.PaymentStatus.PAID;
import static HospitalManagementSystem.SpringProject.entity.Status.PaymentStatus.PENDING;

@Service
@RequiredArgsConstructor
@Transactional
public class BillServiceImplementation implements BillService {
    private final BillRepository billRepository;
    private final PatientRepository patientRepository;

    @Override
    public Bill generateBill(Bill bill) {
        // Validate patient exists
        Patient patient = patientRepository.findById(bill.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Calculate total if not already set
        if (bill.getTotalAmount() == null || bill.getTotalAmount() == 0) {
            double total = bill.getConsultationFee() +
                    bill.getMedicationCharges() +
                    bill.getLabCharges() +
                    bill.getRoomCharges() +
                    bill.getOtherCharges();

            double tax = total * (bill.getTaxPercentage() / 100);
            total = total + tax - (bill.getDiscountAmount() != null ? bill.getDiscountAmount() : 0);

            bill.setTotalAmount(total);
        }

        bill.setPatient(patient);
        bill.setBillDate(LocalDateTime.now());
        bill.setDueDate(bill.getBillDate().plusDays(15));
        bill.setPaymentStatus(PENDING);

        return billRepository.save(bill);
    }

    @Override
    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public List<Bill> getBillsByPatient(Long patientId) {
        return billRepository.findByPatientId(patientId);
    }

    @Override
    public List<Bill> getBillsByPaymentStatus(PaymentStatus paymentStatus) {
        return billRepository.findByPaymentStatus(paymentStatus);
    }

    @Override
    public List<Bill> getTodayBills() {
        LocalDateTime start = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        return billRepository.findByBillDateBetween(start, end);
    }

    @Override
    public List<Bill> getOverdueBills() {
        return billRepository.findOverdueBills();
    }

    @Override
    public Bill updateBill(Long id, Bill billDetails) {
        Bill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + id));

        existingBill.setConsultationFee(billDetails.getConsultationFee());
        existingBill.setMedicationCharges(billDetails.getMedicationCharges());
        existingBill.setLabCharges(billDetails.getLabCharges());
        existingBill.setRoomCharges(billDetails.getRoomCharges());
        existingBill.setOtherCharges(billDetails.getOtherCharges());
        existingBill.setDiscountAmount(billDetails.getDiscountAmount());
        existingBill.setTaxPercentage(billDetails.getTaxPercentage());

        // Recalculate total
        double total = existingBill.getConsultationFee() +
                existingBill.getMedicationCharges() +
                existingBill.getLabCharges() +
                existingBill.getRoomCharges() +
                existingBill.getOtherCharges();

        double tax = total * (existingBill.getTaxPercentage() / 100);
        total = total + tax - (existingBill.getDiscountAmount() != null ? existingBill.getDiscountAmount() : 0);

        existingBill.setTotalAmount(total);

        return billRepository.save(existingBill);
    }

    @Override
    public Bill processPayment(Long id, PaymentMethod paymentMethod) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + id));

        if (PAID.equals(bill.getPaymentStatus())) {
            throw new RuntimeException("Bill is already paid");
        }

        bill.setPaymentStatus(PAID);
        bill.setPaymentDate(LocalDateTime.now());
        bill.setPaymentMethod(paymentMethod);

        return billRepository.save(bill);
    }

    @Override
    public Bill markAsPaid(Long id) {
        return processPayment(id, CASH);
    }

    @Override
    public void deleteBill(Long id) {
        if (!billRepository.existsById(id)) {
            throw new RuntimeException("Bill not found with id: " + id);
        }
        billRepository.deleteById(id);
    }

    @Override
    public double getTotalRevenueForToday() {
        LocalDateTime start = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        Double revenue = billRepository.getTotalRevenueBetweenDates(start, end);
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public double getTotalRevenueForMonth() {
        LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().getMonth().maxLength()).with(LocalTime.MAX);
        Double revenue = billRepository.getTotalRevenueBetweenDates(start, end);
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public long getPendingPaymentsCount() {
        return billRepository.countPendingPayments();
    }

    @Override
    public double getPatientOutstandingBalance(Long patientId) {
        List<Bill> pendingBills = billRepository.findByPatientIdAndPaymentStatus(patientId, PENDING);
        return pendingBills.stream()
                .mapToDouble(Bill::getTotalAmount)
                .sum();
    }
}

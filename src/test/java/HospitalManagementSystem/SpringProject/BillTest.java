package HospitalManagementSystem.SpringProject;

import HospitalManagementSystem.SpringProject.entity.Bill;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentMethod;
import HospitalManagementSystem.SpringProject.entity.Status.PaymentStatus;
import HospitalManagementSystem.SpringProject.record.BillRecord;
import HospitalManagementSystem.SpringProject.service.AppointmentService;
import HospitalManagementSystem.SpringProject.service.BillService;
import HospitalManagementSystem.SpringProject.service.DoctorService;
import HospitalManagementSystem.SpringProject.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BillTest {

    @Autowired
    private BillService billService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    // ========== CREATE ==========
    @Test
    void generateBillTest() {
        Bill bill = new Bill();
        bill.setPatient(patientService.getPatientById(32L).orElse(null));
        bill.setDoctor(doctorService.getDoctorById(31L).orElse(null));
        bill.setAppointment(appointmentService.getAppointmentById(501L).orElse(null));
        bill.setConsultationFee(1000.0);
        bill.setMedicationCharges(500.0);
        bill.setLabCharges(300.0);
        bill.setRoomCharges(2000.0);
        bill.setOtherCharges(100.0);
        bill.setDiscountAmount(100.0);
        bill.setTaxPercentage(18.0);
        bill.setDescription("General consultation");
        Bill generated = billService.generateBill(bill);
        assertThat(generated.getId()).isNotNull();
        assertThat(generated.getTotalAmount()).isNotNull();
        System.out.println("✅ Generated bill ID: " + generated.getId() + " | Amount: " + generated.getTotalAmount());
    }

    // ========== READ ==========
    @Test
    void getBillByIdTest() {
        long start = System.currentTimeMillis();
        Optional<Bill> bill = billService.getBillById(1L);
        long time = System.currentTimeMillis() - start;

        if (bill.isPresent()) {
            System.out.println("getBillById(): " + time + "ms - Bill amount: " + bill.get().getTotalAmount());
        } else {
            System.out.println("getBillById(): " + time + "ms - No bill found with ID 1");
        }
    }

    @Test
    @Transactional
    void getBillsByPatientTest() {
        long start = System.currentTimeMillis();
        List<BillRecord> bills = billService.getBillsByPatient(11L);
        long time = System.currentTimeMillis() - start;

        System.out.println("getBillsByPatient(): " + time + "ms (" + bills.size() + " records)");

        if (!bills.isEmpty()) {
            System.out.println("  First bill - Patient: " + bills.getFirst().getPatientName() +
                    " | Amount: " + bills.getFirst().getTotalAmount());
        }
    }

    @Test
    void getBillsByPaymentStatusTest() {
        long start = System.currentTimeMillis();
        List<BillRecord> pendingBills = billService.getBillsByPaymentStatus(PaymentStatus.PENDING);
        long time = System.currentTimeMillis() - start;

        System.out.println("getBillsByPaymentStatus(PENDING): " + time + "ms (" + pendingBills.size() + " records)");

        List<BillRecord> paidBills = billService.getBillsByPaymentStatus(PaymentStatus.PAID);
        System.out.println("getBillsByPaymentStatus(PAID): " + time + "ms (" + paidBills.size() + " records)");
    }

    @Test
    void getTodayBillsTest() {
        long start = System.currentTimeMillis();
        List<BillRecord> todayBills = billService.getTodayBills();
        long time = System.currentTimeMillis() - start;

        System.out.println("getTodayBills(): " + time + "ms (" + todayBills.size() + " records)");
    }

    @Test
    void getOverdueBillsTest() {
        long start = System.currentTimeMillis();
        List<BillRecord> overdueBills = billService.getOverdueBills();
        long time = System.currentTimeMillis() - start;

        System.out.println("getOverdueBills(): " + time + "ms (" + overdueBills.size() + " records)");
    }

    // ========== REVENUE METHODS ==========
    @Test
    void getTotalRevenueForTodayTest() {
        long start = System.currentTimeMillis();
        double revenue = billService.getTotalRevenueForToday();
        long time = System.currentTimeMillis() - start;

        System.out.println("getTotalRevenueForToday(): " + time + "ms - $" + revenue);
    }

    @Test
    void getTotalRevenueForMonthTest() {
        long start = System.currentTimeMillis();
        double revenue = billService.getTotalRevenueForMonth();
        long time = System.currentTimeMillis() - start;

        System.out.println("getTotalRevenueForMonth(): " + time + "ms - $" + revenue);
    }

    @Test
    void getPendingPaymentsCountTest() {
        long start = System.currentTimeMillis();
        long count = billService.getPendingPaymentsCount();
        long time = System.currentTimeMillis() - start;

        System.out.println("getPendingPaymentsCount(): " + time + "ms - " + count + " pending payments");
    }

    // ========== UPDATE ==========
    @Test
    void processPaymentTest() {
        // First check if there's a pending bill
        List<BillRecord> pendingBills = billService.getBillsByPaymentStatus(PaymentStatus.PENDING);

        if (pendingBills.isEmpty()) {
            System.out.println("⚠️ No pending bills found to process payment");
            return;
        }

        Long billId = pendingBills.getFirst().getId();
        long start = System.currentTimeMillis();
        Bill processed = billService.processPayment(billId, PaymentMethod.CREDIT_CARD);
        long time = System.currentTimeMillis() - start;

        assertThat(processed.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        System.out.println("processPayment(): " + time + "ms - Bill " + billId + " marked as PAID");
    }

    @Test
    void markAsPaidTest() {
        // First check if there's a pending bill
        List<BillRecord> pendingBills = billService.getBillsByPaymentStatus(PaymentStatus.PENDING);

        if (pendingBills.isEmpty()) {
            System.out.println("⚠️ No pending bills found to mark as paid");
            return;
        }

        Long billId = pendingBills.getFirst().getId();

        long start = System.currentTimeMillis();
        Bill marked = billService.markAsPaid(billId);
        long time = System.currentTimeMillis() - start;

        assertThat(marked.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        System.out.println("markAsPaid(): " + time + "ms - Bill " + billId + " marked as PAID");
    }

    // ========== PERFORMANCE TEST ==========
    @Test
    void testAllBillServicesPerformance() {
        System.out.println("\n========== BILL PERFORMANCE TEST ==========");

        // Get by patient
        long start = System.currentTimeMillis();
        List<BillRecord> byPatient = billService.getBillsByPatient(1L);
        long time1 = System.currentTimeMillis() - start;
        System.out.println("getBillsByPatient(): " + time1 + "ms (" + byPatient.size() + " records)");

        // Get pending
        start = System.currentTimeMillis();
        List<BillRecord> pending = billService.getBillsByPaymentStatus(PaymentStatus.PENDING);
        long time2 = System.currentTimeMillis() - start;
        System.out.println("getBillsByPaymentStatus(): " + time2 + "ms (" + pending.size() + " records)");

        // Today's revenue
        start = System.currentTimeMillis();
        double todayRevenue = billService.getTotalRevenueForToday();
        long time3 = System.currentTimeMillis() - start;
        System.out.println("getTotalRevenueForToday(): " + time3 + "ms ($" + todayRevenue + ")");

        // Month revenue
        start = System.currentTimeMillis();
        double monthRevenue = billService.getTotalRevenueForMonth();
        long time4 = System.currentTimeMillis() - start;
        System.out.println("getTotalRevenueForMonth(): " + time4 + "ms ($" + monthRevenue + ")");

        // Pending count
        start = System.currentTimeMillis();
        long pendingCount = billService.getPendingPaymentsCount();
        long time5 = System.currentTimeMillis() - start;
        System.out.println("getPendingPaymentsCount(): " + time5 + "ms (" + pendingCount + ")");

        System.out.println("✅ All bill performance tests completed!");
    }
}

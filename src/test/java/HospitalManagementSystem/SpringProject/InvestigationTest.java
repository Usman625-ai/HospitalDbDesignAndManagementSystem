package HospitalManagementSystem.SpringProject;

import HospitalManagementSystem.SpringProject.entity.Investigation;
import HospitalManagementSystem.SpringProject.entity.Status.InvestigationStatus;
import HospitalManagementSystem.SpringProject.entity.Status.InvestigationType;
import HospitalManagementSystem.SpringProject.entity.Status.UrgencyStatus;
import HospitalManagementSystem.SpringProject.service.InvestigationService;
import HospitalManagementSystem.SpringProject.service.PatientService;
import HospitalManagementSystem.SpringProject.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class InvestigationTest {

    @Autowired
    private InvestigationService investigationService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    // ========== CREATE ==========
    @Test
    void requestInvestigationTest() {
        Investigation investigation = new Investigation();
        investigation.setPatient(patientService.getPatientById(50L).orElse(null));
        investigation.setDoctor(doctorService.getDoctorById(40L).orElse(null));
        investigation.setInvestigationType(InvestigationType.BLOOD);
        investigation.setDescription("Complete blood count, lipid profile");
        investigation.setUrgency(UrgencyStatus.ROUTINE);
        investigation.setNotes("Patient has high cholesterol symptoms");

        Investigation saved = investigationService.requestInvestigation(investigation);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(InvestigationStatus.PENDING);
        System.out.println("✅ Requested investigation ID: " + saved.getId() + " - Type: " + saved.getInvestigationType());
    }

    // ========== READ ==========
    @Test
    void getInvestigationByIdTest() {
        long start = System.currentTimeMillis();
        Optional<Investigation> investigation = investigationService.getInvestigationById(1L);
        long time = System.currentTimeMillis() - start;

        if (investigation.isPresent()) {
            System.out.println("getInvestigationById(): " + time + "ms - " + investigation.get().getInvestigationType());
        } else {
            System.out.println("getInvestigationById(): " + time + "ms - No investigation found with ID 1");
        }
    }

    @Test
    void getAllInvestigationsTest() {
        long start = System.currentTimeMillis();
        List<Investigation> investigations = investigationService.getAllInvestigations();
        long time = System.currentTimeMillis() - start;

        System.out.println("getAllInvestigations(): " + time + "ms (" + investigations.size() + " records)");
        assertThat(investigations).isNotEmpty();
    }

    @Test
    void getInvestigationsByPatientTest() {
        long start = System.currentTimeMillis();
        List<Investigation> investigations = investigationService.getInvestigationsByPatient(40L);
        long time = System.currentTimeMillis() - start;

        System.out.println("getInvestigationsByPatient(): " + time + "ms (" + investigations.size() + " records)");
    }

    @Test
    void getPendingInvestigationsTest() {
        long start = System.currentTimeMillis();
        List<Investigation> pending = investigationService.getPendingInvestigations(40L);
        long time = System.currentTimeMillis() - start;

        System.out.println("getPendingInvestigations(): " + time + "ms (" + pending.size() + " records)");
    }

    // ========== UPDATE ==========
    @Test
    void updateInvestigationTest() {
        Optional<Investigation> existing = investigationService.getInvestigationById(1L);

        if (existing.isPresent()) {
            Investigation inv = existing.get();
            String oldDesc = inv.getDescription();
            inv.setDescription("Updated: Full blood count, lipid profile, thyroid test");
            inv.setUrgency(UrgencyStatus.URGENT);

            long start = System.currentTimeMillis();
            Investigation updated = investigationService.updateInvestigation(1L, inv);
            long time = System.currentTimeMillis() - start;

            System.out.println("updateInvestigation(): " + time + "ms - Description updated from '" + oldDesc + "' to '" + updated.getDescription() + "'");
        } else {
            System.out.println("⚠️ No investigation found with ID 1 to update");
        }
    }

    @Test
    void addResultsTest() {
        // First get a pending investigation
        List<Investigation> pending = investigationService.getPendingInvestigations(1L);

        if (!pending.isEmpty()) {
            Long id = pending.getFirst().getId();

            long start = System.currentTimeMillis();
            Investigation updated = investigationService.addResults(id,
                    "Hb: 14.5, WBC: 7200, Platelets: 250000, Cholesterol: 180",
                    "/reports/patient1/cbc_report.pdf");
            long time = System.currentTimeMillis() - start;

            assertThat(updated.getStatus()).isEqualTo(InvestigationStatus.COMPLETED);
            assertThat(updated.getResults()).isNotNull();
            System.out.println("addResults(): " + time + "ms - Investigation " + id + " completed, results added");
        } else {
            System.out.println("⚠️ No pending investigations found to add results");
        }
    }

    @Test
    void updateStatusTest() {
        Optional<Investigation> existing = investigationService.getInvestigationById(1L);

        if (existing.isPresent()) {
            InvestigationStatus newStatus = InvestigationStatus.IN_PROGRESS;

            long start = System.currentTimeMillis();
            Investigation updated = investigationService.updateStatus(1L, newStatus);
            long time = System.currentTimeMillis() - start;

            System.out.println("updateStatus(): " + time + "ms - Status changed to: " + updated.getStatus());
        } else {
            System.out.println("⚠️ No investigation found with ID 1");
        }
    }

    // ========== DELETE ==========
    @Test
    void deleteInvestigationTest() {
        // First create a temporary investigation to delete
        Investigation temp = new Investigation();
        temp.setPatient(patientService.getPatientById(31L).orElse(null));
        temp.setDoctor(doctorService.getDoctorById(33L).orElse(null));
        temp.setInvestigationType(InvestigationType.XRAY);
        temp.setDescription("Chest X-ray");
        temp.setUrgency(UrgencyStatus.ROUTINE);
        temp.setStatus(InvestigationStatus.PENDING);

        Investigation saved = investigationService.requestInvestigation(temp);
        Long id = saved.getId();
        System.out.println("Created temporary investigation with ID: " + id);

        long start = System.currentTimeMillis();
        investigationService.deleteInvestigation(id);
        long time = System.currentTimeMillis() - start;

        Optional<Investigation> deleted = investigationService.getInvestigationById(id);
        assertThat(deleted).isEmpty();
        System.out.println("deleteInvestigation(): " + time + "ms - Investigation ID " + id + " deleted successfully");
    }

    // ========== BUSINESS METHODS ==========
    @Test
    void getPendingCountTest() {
        long start = System.currentTimeMillis();
        long count = investigationService.getPendingCount();
        long time = System.currentTimeMillis() - start;

        System.out.println("getPendingCount(): " + time + "ms - " + count + " pending investigations");
    }

    // ========== PERFORMANCE TEST ==========
    @Test
    void testAllInvestigationServicesPerformance() {
        System.out.println("\n========== INVESTIGATION PERFORMANCE TEST ==========");

        // Get all
        long start = System.currentTimeMillis();
        List<Investigation> all = investigationService.getAllInvestigations();
        long time1 = System.currentTimeMillis() - start;
        System.out.println("getAllInvestigations(): " + time1 + "ms (" + all.size() + " records)");

        // Get by patient
        start = System.currentTimeMillis();
        List<Investigation> byPatient = investigationService.getInvestigationsByPatient(1L);
        long time2 = System.currentTimeMillis() - start;
        System.out.println("getInvestigationsByPatient(): " + time2 + "ms (" + byPatient.size() + " records)");

        // Get pending
        start = System.currentTimeMillis();
        List<Investigation> pending = investigationService.getPendingInvestigations(1L);
        long time3 = System.currentTimeMillis() - start;
        System.out.println("getPendingInvestigations(): " + time3 + "ms (" + pending.size() + " records)");

        // Pending count
        start = System.currentTimeMillis();
        long pendingCount = investigationService.getPendingCount();
        long time4 = System.currentTimeMillis() - start;
        System.out.println("getPendingCount(): " + time4 + "ms (" + pendingCount + ")");

        System.out.println("✅ All investigation performance tests completed!");
    }
}
package HospitalManagementSystem.SpringProject;

import HospitalManagementSystem.SpringProject.entity.MedicalRecord;
import HospitalManagementSystem.SpringProject.entity.Status.MedicalRecordType;
import HospitalManagementSystem.SpringProject.service.MedicalRecordService;
import HospitalManagementSystem.SpringProject.service.PatientService;
import HospitalManagementSystem.SpringProject.service.DoctorService;
import HospitalManagementSystem.SpringProject.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MedicalRecordTest {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    // ========== CREATE ==========
    @Test
    void addMedicalRecordTest() {
        MedicalRecord record = new MedicalRecord();
        record.setPatient(patientService.getPatientById(31L).orElse(null));
        record.setDoctor(doctorService.getDoctorById(31L).orElse(null));
        record.setAppointment(appointmentService.getAppointmentById(4L).orElse(null));
        record.setRecordType(MedicalRecordType.GENERAL);
        record.setDiagnosis("Acute bronchitis with fever");
        record.setTreatment("Prescribed antibiotics and rest for 5 days");
        record.setNotes("Patient responded well to initial treatment");
        record.setBloodPressure("120/80");
        record.setHeartRate("72");
        record.setTemperature("98.6");
        record.setWeight("70.5");
        record.setHeight("170");
        record.setAllergies("None known");
        record.setPastSurgeries("Appendectomy (2015)");
        record.setFamilyHistory("Father has diabetes");

        MedicalRecord saved = medicalRecordService.addMedicalRecord(record);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getRecordNumber()).isNotNull();
        System.out.println("✅ Added medical record: " + saved.getRecordNumber() + " - Diagnosis: " + saved.getDiagnosis());
    }

    // ========== READ ==========
    @Test
    void getMedicalRecordByIdTest() {
        long start = System.currentTimeMillis();
        Optional<MedicalRecord> record = medicalRecordService.getMedicalRecordById(11L);
        long time = System.currentTimeMillis() - start;

        if (record.isPresent()) {
            System.out.println("getMedicalRecordById(): " + time + "ms - Record: " + record.get().getRecordNumber());
        } else {
            System.out.println("getMedicalRecordById(): " + time + "ms - No record found with ID 1");
        }
    }

    @Test
    void getAllMedicalRecordsTest() {
        long start = System.currentTimeMillis();
        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
        long time = System.currentTimeMillis() - start;

        System.out.println("getAllMedicalRecords(): " + time + "ms (" + records.size() + " records)");
        assertThat(records).isNotEmpty();
    }

    @Test
    void getMedicalRecordsByPatientTest() {
        long start = System.currentTimeMillis();
        List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByPatient(12L);
        long time = System.currentTimeMillis() - start;

        System.out.println("getMedicalRecordsByPatient(): " + time + "ms (" + records.size() + " records)");

        if (!records.isEmpty()) {
            System.out.println("  First record - Diagnosis: " + records.getFirst().getDiagnosis());
        }
    }

    @Test
    void getPatientMedicalHistoryTest() {
        long start = System.currentTimeMillis();
        List<MedicalRecord> history = medicalRecordService.getPatientMedicalHistory(21L);
        long time = System.currentTimeMillis() - start;

        System.out.println("getPatientMedicalHistory(): " + time + "ms (" + history.size() + " records)");

        if (!history.isEmpty()) {
            System.out.println("  Medical history includes:");
            history.forEach(record ->
                    System.out.println("    - " + record.getDiagnosis() + " (" + record.getRecordDate() + ")")
            );
        }
    }

    // ========== UPDATE ==========
    @Test
    void updateMedicalRecordTest() {
        Optional<MedicalRecord> existing = medicalRecordService.getMedicalRecordById(1L);

        if (existing.isPresent()) {
            MedicalRecord record = existing.get();
            String oldTreatment = record.getTreatment();
            String oldNotes = record.getNotes();

            record.setTreatment(oldTreatment + " - Follow up after 1 week");
            record.setNotes(oldNotes + " Patient advised to return if symptoms worsen");
            record.setBloodPressure("118/78");
            record.setHeartRate("70");

            long start = System.currentTimeMillis();
            MedicalRecord updated = medicalRecordService.updateMedicalRecord(7L, record);
            long time = System.currentTimeMillis() - start;

            System.out.println("updateMedicalRecord(): " + time + "ms - Updated treatment and notes");
            System.out.println("  Old treatment: " + oldTreatment);
            System.out.println("  New treatment: " + updated.getTreatment());
        } else {
            System.out.println("⚠️ No medical record found with ID 7 to update");
        }
    }

    // ========== DELETE ==========
    @Test
    void deleteMedicalRecordTest() {
        // First create a temporary record to delete
        MedicalRecord temp = new MedicalRecord();
        temp.setPatient(patientService.getPatientById(41L).orElse(null));
        temp.setDoctor(doctorService.getDoctorById(32L).orElse(null));
        temp.setRecordType(MedicalRecordType.GENERAL);
        temp.setDiagnosis("Temporary record for deletion test");
        temp.setTreatment("Test treatment");
        temp.setNotes("This record will be deleted");

        MedicalRecord saved = medicalRecordService.addMedicalRecord(temp);
        Long id = saved.getId();
        System.out.println("Created temporary medical record with ID: " + id);

        long start = System.currentTimeMillis();
        medicalRecordService.deleteMedicalRecord(id);
        long time = System.currentTimeMillis() - start;

        Optional<MedicalRecord> deleted = medicalRecordService.getMedicalRecordById(id);
        assertThat(deleted).isEmpty();
        System.out.println("deleteMedicalRecord(): " + time + "ms - Record ID " + id + " deleted successfully");
    }

    // ========== PERFORMANCE TEST ==========
    @Test
    void testAllMedicalRecordServicesPerformance() {
        System.out.println("\n========== MEDICAL RECORD PERFORMANCE TEST ==========");

        // Get all records
        long start = System.currentTimeMillis();
        List<MedicalRecord> all = medicalRecordService.getAllMedicalRecords();
        long time1 = System.currentTimeMillis() - start;
        System.out.println("getAllMedicalRecords(): " + time1 + "ms (" + all.size() + " records)");

        // Get by patient
        start = System.currentTimeMillis();
        List<MedicalRecord> byPatient = medicalRecordService.getMedicalRecordsByPatient(1L);
        long time2 = System.currentTimeMillis() - start;
        System.out.println("getMedicalRecordsByPatient(): " + time2 + "ms (" + byPatient.size() + " records)");

        // Get medical history
        start = System.currentTimeMillis();
        List<MedicalRecord> history = medicalRecordService.getPatientMedicalHistory(1L);
        long time3 = System.currentTimeMillis() - start;
        System.out.println("getPatientMedicalHistory(): " + time3 + "ms (" + history.size() + " records)");

        System.out.println("✅ All medical record performance tests completed!");
    }
}

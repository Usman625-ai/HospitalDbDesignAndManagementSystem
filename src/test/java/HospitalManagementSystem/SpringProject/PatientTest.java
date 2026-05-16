package HospitalManagementSystem.SpringProject;

import HospitalManagementSystem.SpringProject.entity.Patient;
import HospitalManagementSystem.SpringProject.entity.Status.PatientStatus;
import HospitalManagementSystem.SpringProject.record.PatientRecord;
import HospitalManagementSystem.SpringProject.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PatientTest {

    @Autowired
    private PatientService patientService;

    // ========== CREATE ==========
    @Test
    void RegisterPatientTest() {
        Patient p = new Patient();
        p.setFirstName("alishan");
        p.setLastName("hussain");
        p.setEmail("mralishanhussain101@gmail.com");
        p.setPhoneNumber("0335353652");
        Patient saved = patientService.registerPatient(p);
        System.out.println(saved.getPatientId() + " - name is " + saved.getFirstName());
    }

    // ========== READ ==========
    @Test
    void getAllPatients() {
        long start = System.currentTimeMillis();
        List<PatientRecord> patients = patientService.getAllPatients();
        long time = System.currentTimeMillis() - start;
        System.out.println("getAllPatients(): " + time + "ms (" + patients.size() + " records)");
    }

    @Test
    void getByStatus() {
        long start = System.currentTimeMillis();
        List<PatientRecord> activePatients = patientService.getPatientsByStatus(PatientStatus.ACTIVE);
        long time = System.currentTimeMillis() - start;
        System.out.println("getPatientsByStatus(): " + time + "ms (" + activePatients.size() + " records)");
    }

    @Test
    void getByLastName() {
        long start = System.currentTimeMillis();
        List<PatientRecord> searched = patientService.searchPatientsByLastName("a");
        long time = System.currentTimeMillis() - start;
        System.out.println("searchPatientsByLastName(): " + time + "ms (" + searched.size() + " records)");
    }

    // ========== UPDATE ==========
//    @Test
//    void UpdatePatientStatus() {
//        // Get old patient first
//        Optional<Patient> oldPatientOpt = patientService.getPatientById(8L);
//
//        if (oldPatientOpt.isPresent()) {
//            Patient oldPatient = oldPatientOpt.get();
//            PatientStatus oldStatus = oldPatient.getStatus();
//            System.out.println("Old patient: " + oldPatient.getFirstName() + " | Status: " + oldStatus);
//
//            // Update status to DISCHARGED
//            Patient updatedPatient = patientService.updatePatientStatus(8L, DISCHARGED);
//            System.out.println("Updated patient: " + updatedPatient.getFirstName() + " | New Status: " + updatedPatient.getStatus());
//        } else {
//            System.out.println("Patient with ID 8 not found!");
//        }
//    }
}

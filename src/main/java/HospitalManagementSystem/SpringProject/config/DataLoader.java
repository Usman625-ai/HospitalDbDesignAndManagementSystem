package HospitalManagementSystem.SpringProject.config;

import HospitalManagementSystem.SpringProject.entity.*;
import HospitalManagementSystem.SpringProject.entity.Status.*;
import HospitalManagementSystem.SpringProject.repository.*;
import HospitalManagementSystem.SpringProject.service.*;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.ZoneId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static HospitalManagementSystem.SpringProject.entity.Status.AppointmentStatus.*;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicineRepository medicineRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final InvestigationRepository investigationRepository;

    private final Faker faker = new Faker(Locale.of("en"));
    private final Random random = new Random();

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private BillService billService;

    @Autowired
    private InvestigationService investigationService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    private final PrescriptionMedicineRepository prescriptionMedicineRepository;


    @Override
    @Transactional
    public void run(String... args) {
        // Check if data already exists
        if (patientRepository.count() > 0) {
            System.out.println("✓ Data already exists. Skipping data load.");
            return;
        }

        try {
            System.out.println("\n🔄 Starting fresh data generation...\n");

            // 1. Create Departments
            List<Department> departments = createDepartments();
            System.out.println("✅ Departments: " + departments.size());

            // 2. Create Doctors
            List<Doctor> doctors = createDoctors(departments);
            System.out.println("✅ Doctors: " + doctors.size());

            // 3. Create Patients
            List<Patient> patients = createPatients();
            System.out.println("✅ Patients: " + patients.size());

            // 4. Create Appointments
            List<Appointment> appointments = createAppointments(patients, doctors);
            System.out.println("✅ Appointments: " + appointments.size());

            // 5. Create Prescriptions
            List<Prescription> prescriptions = createPrescriptions(patients, doctors, appointments);
            System.out.println("✅ Prescriptions: " + prescriptions.size());

            // 6. Create Medicines
            int medicineCount = createMedicines(prescriptions);
            System.out.println("✅ Medicines: " + medicineCount);

            // 7. Create Bills
            List<Bill> bills = createBills(patients, doctors, appointments);
            System.out.println("✅ Bills: " + bills.size());

            // 8. Create Medical Records
            List<MedicalRecord> medicalRecords = createMedicalRecords(patients, doctors, appointments);
            System.out.println("✅ Medical Records: " + medicalRecords.size());

            // 9. Create Investigations
            int investigationCount = createInvestigations(patients, doctors);
            System.out.println("✅ Investigations: " + investigationCount);

            System.out.println("\n✅ ALL DATA GENERATED SUCCESSFULLY!");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR: " + e.getMessage());
            System.err.println("Rolling back all changes...");
            throw new RuntimeException("Data loading failed - rolling back", e);
        }
    }

    private List<Department> createDepartments() {
        List<Department> departments = new ArrayList<>();

        String[][] deptData = {
                {"Cardiology", "Heart and cardiovascular diseases", "3", "20", "15", "09:00", "17:00", "5000000"},
                {"Neurology", "Brain and nervous system disorders", "4", "15", "10", "09:00", "18:00", "4500000"},
                {"Orthopedics", "Bone and joint care", "2", "25", "20", "09:00", "16:00", "4000000"},
                {"Pediatrics", "Child healthcare", "1", "30", "25", "09:00", "20:00", "3500000"},
                {"Gynecology", "Women's health", "5", "20", "15", "09:00", "17:00", "4200000"},
                {"Emergency", "24/7 Emergency care", "0", "40", "10", "00:00", "23:59", "8000000"}
        };

        for (String[] data : deptData) {
            Department dept = new Department();
            dept.setName(data[0]);
            dept.setDescription(data[1]);
            dept.setFloorNumber(Integer.parseInt(data[2]));
            dept.setTotalBeds(Integer.parseInt(data[3]));
            dept.setAvailableBeds(Integer.parseInt(data[4]));
            dept.setOpeningTime(data[5]);
            dept.setClosingTime(data[6]);
            dept.setAnnualBudget(new BigDecimal(data[7]));
            dept.setPhoneNumber(faker.phoneNumber().cellPhone());
            dept.setEmail(data[0].toLowerCase() + "@hospital.com");
            dept.setIs24x7(data[0].equals("Emergency"));
            dept.setEmergencyAvailable(data[0].equals("Emergency"));
            departments.add(departmentService.addDepartment(dept));
        }
        return departments;
    }

    private List<Doctor> createDoctors(List<Department> departments) {
        List<Doctor> doctors = new ArrayList<>();
        String[] specializations = {"Cardiologist", "Neurosurgeon", "Orthopedic Surgeon", "Pediatrician", "Gynecologist", "Emergency Physician"};
        String[] qualifications = {"MBBS, MD", "MBBS, MS", "MBBS, DNB", "MBBS, FCPS", "MBBS, PhD"};

        for (int i = 0; i < 30; i++) {
            Doctor doctor = new Doctor();
            doctor.setName(faker.name().fullName());
            doctor.setEmail(faker.internet().emailAddress());
            doctor.setPhoneNumber(faker.phoneNumber().cellPhone());
            doctor.setMobileNumber(faker.phoneNumber().cellPhone());
            doctor.setDateOfBirth(faker.date().birthday(30, 65).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            doctor.setGender(faker.options().option(Gender.class));
            doctor.setSpecialization(specializations[i % specializations.length]);
            doctor.setQualification(qualifications[i % qualifications.length]);
            doctor.setExperienceYears(faker.number().numberBetween(1, 30));
            doctor.setDepartment(departments.get(i % departments.size()));
            doctor.setConsultationFee(BigDecimal.valueOf(faker.number().numberBetween(500, 3000)));
            doctor.setFollowUpFee(BigDecimal.valueOf(faker.number().numberBetween(300, 1500)));
            doctor.setWorkingDays("MON,TUE,WED,THU,FRI");
            doctor.setStartTime("09:00");
            doctor.setEndTime("17:00");
            doctor.setActive(true);
            doctor.setStatus(DoctorStatus.AVAILABLE);
            doctor.setAverageRating(4.0 + random.nextDouble());
            doctor.setTotalReviews(faker.number().numberBetween(10, 500));
            doctors.add(doctorService.registerDoctor(doctor));
        }
        return doctors;
    }

    private List<Patient> createPatients() {
        List<Patient> patients = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            Patient patient = new Patient();
            patient.setFirstName(faker.name().firstName());
            patient.setLastName(faker.name().lastName());
            patient.setEmail(faker.internet().emailAddress());
            patient.setPhoneNumber(faker.phoneNumber().cellPhone());
            patient.setDateOfBirth(faker.date().birthday(1, 90).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            patient.setGender(faker.options().option(Gender.class));
            patient.setBloodGroup(faker.options().option(BloodGroup.class));
            patient.setAddress(faker.address().fullAddress());
            patient.setEmergencyContact(faker.phoneNumber().cellPhone());
            patient.setStatus(PatientStatus.ACTIVE);
            // patientId will be auto-generated by trigger
            patients.add(patientService.registerPatient(patient));
        }
        return patients;
    }

    private List<Appointment> createAppointments(List<Patient> patients, List<Doctor> doctors) {
        List<Appointment> appointments = new ArrayList<>();
        AppointmentStatus[] statuses = {SCHEDULED,
                CONFIRMED,
                CHECKED_IN,
                IN_PROGRESS,
                COMPLETED,
                CANCELLED,
                NO_SHOW,
                RESCHEDULED,
                WAITING};

        for (int i = 0; i < 800; i++) {
            Appointment appointment = new Appointment();
            appointment.setPatient(patients.get(random.nextInt(patients.size())));
            appointment.setDoctor(doctors.get(random.nextInt(doctors.size())));
            appointment.setAppointmentDateTime(LocalDateTime.now()
                    .minusDays(random.nextInt(90))
                    .plusHours(random.nextInt(24)));

            // Make 80% of appointments COMPLETED
            if (i < 640) {  // 80% of 800 = 640
                appointment.setStatus(COMPLETED);
            } else {
                appointment.setStatus(statuses[random.nextInt(statuses.length)]);
            }

            appointment.setSymptoms(faker.medical().symptoms());
            appointment.setNotes("Patient complains of " + faker.medical().symptoms());
            appointments.add(appointmentService.bookAppointment(appointment));
        }
        return appointments;
    }

    private List<Prescription> createPrescriptions(List<Patient> patients, List<Doctor> doctors, List<Appointment> appointments) {
        List<Prescription> prescriptions = new ArrayList<>();
        List<Appointment> completedAppointments = appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .toList();

        for (int i = 0; i < 600 && i < completedAppointments.size(); i++) {
            Prescription prescription = new Prescription();
            prescription.setPatient(completedAppointments.get(i).getPatient());
            prescription.setDoctor(completedAppointments.get(i).getDoctor());
            prescription.setAppointment(completedAppointments.get(i));
            prescription.setDiagnosis(faker.medical().diseaseName());
            prescription.setGeneralAdvice("Take rest and follow medication schedule");
            prescription.setPrescriptionDate(LocalDateTime.now().minusDays(random.nextInt(60)));
            prescription.setCreatedBy(completedAppointments.get(i).getDoctor().getName());
            prescriptions.add(prescriptionService.createPrescription(prescription));
        }
        return prescriptions;
    }

    private int createMedicines(List<Prescription> prescriptions) {
        int count = 0;

        // First create master medicines (saved to database)
        List<Medicine> masterMedicines = createMasterMedicines();

        // Then link them to prescriptions via PrescriptionMedicine
        for (Prescription prescription : prescriptions) {
            int numMedicines = random.nextInt(3) + 1;
            List<Medicine> shuffled = new ArrayList<>(masterMedicines);
            Collections.shuffle(shuffled);

            for (int i = 0; i < numMedicines; i++) {
                Medicine medicine = shuffled.get(i);

                // Create PrescriptionMedicine (join entity with extra fields)
                PrescriptionMedicine pm = new PrescriptionMedicine();
                pm.setPrescription(prescription);
                pm.setMedicine(medicine);
                pm.setDosage(faker.options().option("500mg", "250mg", "100mg", "50mg"));
                pm.setFrequency(faker.options().option("Once daily", "Twice daily", "Three times daily"));
                pm.setDuration(faker.options().option("5 days", "7 days", "10 days", "14 days"));
                pm.setQuantity(random.nextInt(30) + 10);
                pm.setIsSubstituteAllowed(random.nextBoolean());
                pm.setPrescribedAt(LocalDateTime.now());

                prescriptionMedicineRepository.save(pm);
                count++;
            }
        }
        return count;
    }

    private List<Medicine> createMasterMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        String[] medicineNames = {"Paracetamol", "Ibuprofen", "Amoxicillin", "Azithromycin", "Metformin",
                "Lisinopril", "Atorvastatin", "Omeprazole", "Losartan", "Ciprofloxacin"};
        Double[] prices = {50.0, 100.0, 150.0, 200.0, 80.0, 120.0, 180.0, 90.0, 110.0, 160.0};

        for (int i = 0; i < medicineNames.length; i++) {
            Medicine medicine = new Medicine();
            medicine.setMedicineName(medicineNames[i]);
            medicine.setUnitPrice(prices[i]);
            medicine.setStatus(MedicineStatus.ACTIVE);
            medicine.setCategory(MedicineCategory.TABLET);
            medicines.add(medicineService.addMedicine(medicine));  // Save via service
        }
        return medicines;
    }

    private List<Bill> createBills(List<Patient> patients, List<Doctor> doctors, List<Appointment> appointments) {
        List<Bill> bills = new ArrayList<>();

        for (int i = 0; i < 500 && i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            Bill bill = new Bill();
            bill.setPatient(appointment.getPatient());
            bill.setDoctor(appointment.getDoctor());
            bill.setAppointment(appointment);
            bill.setConsultationFee(Double.valueOf(random.nextInt(1500) + 500));
            bill.setMedicationCharges(Double.valueOf(random.nextInt(5000) + 100));
            bill.setLabCharges(Double.valueOf(random.nextInt(3000) + 200));
            bill.setRoomCharges(Double.valueOf(random.nextInt(10000) + 1000));
            bill.setOtherCharges(Double.valueOf(random.nextInt(1000)));
            bill.setDiscountAmount(Double.valueOf(random.nextInt(500)));
            bill.setTaxPercentage(18.0);

            double subtotal = bill.getConsultationFee() + bill.getMedicationCharges() +
                    bill.getLabCharges() + bill.getRoomCharges() + bill.getOtherCharges();
            double tax = subtotal * 0.18;
            double total = subtotal + tax - bill.getDiscountAmount();
            bill.setTotalAmount(total);

            bill.setPaymentStatus(random.nextBoolean() ? PaymentStatus.PAID : PaymentStatus.PENDING);
            bill.setBillDate(LocalDateTime.now().minusDays(random.nextInt(60)));
            bill.setDueDate(bill.getBillDate().plusDays(15));

            bills.add(billService.generateBill(bill));
        }
        return bills;
    }

    private List<MedicalRecord> createMedicalRecords(List<Patient> patients, List<Doctor> doctors, List<Appointment> appointments) {
        List<MedicalRecord> records = new ArrayList<>();

        for (int i = 0; i < 400 && i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            MedicalRecord record = new MedicalRecord();
            record.setPatient(appointment.getPatient());
            record.setDoctor(appointment.getDoctor());
            record.setAppointment(appointment);
            record.setRecordType(MedicalRecordType.GENERAL);
            record.setDiagnosis(faker.medical().diseaseName());
            record.setTreatment("Prescribed " + faker.medical().medicineName());
            record.setBloodPressure(faker.number().numberBetween(90, 140) + "/" + faker.number().numberBetween(60, 90));
            record.setHeartRate(String.valueOf(faker.number().numberBetween(60, 100)));
            record.setTemperature(String.valueOf(36 + random.nextDouble() * 4));
            record.setAllergies("None");
            records.add(medicalRecordService.addMedicalRecord(record));
        }
        return records;
    }

    private int createInvestigations(List<Patient> patients, List<Doctor> doctors) {
        int count = 0;

        for (int i = 0; i < 300; i++) {
            Investigation investigation = new Investigation();
            investigation.setPatient(patients.get(random.nextInt(patients.size())));
            investigation.setDoctor(doctors.get(random.nextInt(doctors.size())));
            investigation.setInvestigationType(faker.options().option(InvestigationType.class));
            investigation.setDescription("Routine " + investigation.getInvestigationType());
            investigation.setUrgency(UrgencyStatus.ROUTINE);
            investigation.setStatus(random.nextBoolean() ? InvestigationStatus.COMPLETED : InvestigationStatus.PENDING);
            investigation.setResults("Normal results");
            investigation.setRequestedDate(LocalDateTime.now().minusDays(random.nextInt(30)));
            if (investigation.getStatus() == InvestigationStatus.COMPLETED) {
                investigation.setResultDate(investigation.getRequestedDate().plusDays(random.nextInt(5)));
            }
            Investigation inv = investigationService.requestInvestigation(investigation);
            count++;
        }
        return count;
    }

    private void printSummary() {
        System.out.println("\n========== DATABASE SUMMARY ==========");
        System.out.println("Departments:      " + departmentRepository.count());
        System.out.println("Doctors:          " + doctorRepository.count());
        System.out.println("Patients:         " + patientRepository.count());
        System.out.println("Appointments:     " + appointmentRepository.count());
        System.out.println("Prescriptions:    " + prescriptionRepository.count());
        System.out.println("Medicines:        " + medicineRepository.count());
        System.out.println("Bills:            " + billRepository.count());
        System.out.println("Medical Records:  " + medicalRecordRepository.count());
        System.out.println("Investigations:   " + investigationRepository.count());
        System.out.println("======================================\n");
    }
}
package HospitalManagementSystem.SpringProject.controller;

import HospitalManagementSystem.SpringProject.entity.*;
import HospitalManagementSystem.SpringProject.entity.Status.*;
import HospitalManagementSystem.SpringProject.repository.*;
import HospitalManagementSystem.SpringProject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HospitalController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final BillService billService;
    private final PrescriptionService prescriptionService;
    private final MedicineService medicineService;
    private final DepartmentService departmentService;
    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    // ========== DASHBOARD ==========
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalPatients", patientService.getTotalPatientCount());
        model.addAttribute("totalDoctors", doctorService.getTotalDoctorCount());
        model.addAttribute("totalAppointments", appointmentService.getAllAppointments().getTotalElements());
        model.addAttribute("TotalDepartments", departmentService.getDepartmentCount());
        return "dashboard";
    }

    // ========== PATIENT MANAGEMENT ==========
    @GetMapping("/patients")
    public String listPatients(Model model) {
        model.addAttribute("patients", patientRepository.findAll());  // Returns List<PatientRecord> - works
        return "patients";
    }

    @GetMapping("/patients/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-form";
    }

    @PostMapping("/patients/save")
    public String savePatient(@ModelAttribute Patient patient) {
        patientService.registerPatient(patient);
        return "redirect:/patients";
    }

    @GetMapping("/patients/edit/{id}")
    public String showEditPatientForm(@PathVariable Long id, Model model) {
        patientService.getPatientById(id).ifPresent(patient ->
                model.addAttribute("patient", patient));
        return "patient-form";
    }

    @GetMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }

    // ========== DOCTOR MANAGEMENT ==========
    @GetMapping("/doctors")
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorRepository.findAll());
        return "doctors";
    }

    @GetMapping("/doctors/add")
    public String showAddDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctor-form";
    }

    @PostMapping("/doctors/save")
    public String saveDoctor(@ModelAttribute Doctor doctor) {
        doctorService.registerDoctor(doctor);
        return "redirect:/doctors";
    }

    // ========== APPOINTMENT MANAGEMENT ==========
    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentRepository.findAll());
        return "appointments";
    }

    @GetMapping("/appointments/add")
    public String showAddAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "appointment-form";
    }

    @PostMapping("/appointments/save")
    public String saveAppointment(@ModelAttribute Appointment appointment) {
        appointmentService.bookAppointment(appointment);
        return "redirect:/appointments";
    }

    // ========== BILL MANAGEMENT ==========
    @GetMapping("/bills")
    public String listBills(Model model) {
        model.addAttribute("bills", billRepository.findAll());
        model.addAttribute("totalRevenue", billService.getTotalRevenueForMonth());
        return "bills";
    }

    @GetMapping("/bills/pay/{id}")
    public String payBill(@PathVariable Long id) {
        billService.markAsPaid(id);
        return "redirect:/bills";
    }

    // ========== PRESCRIPTION MANAGEMENT ==========
    @GetMapping("/prescriptions")
    public String listPrescriptions(Model model) {
        // Use getPrescriptionsByPatient(0L) or create a method to get all prescriptions as entities
        // For now, get prescriptions for a sample patient
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        model.addAttribute("prescriptions", prescriptions);
        return "prescriptions";
    }

    @GetMapping("/prescriptions/patient/{patientId}")
    public String prescriptionsByPatient(@PathVariable Long patientId, Model model) {
        List<Prescription> prescriptions = prescriptionService.getActivePrescriptionsForPatient(patientId);
        model.addAttribute("prescriptions", prescriptions);
        patientService.getPatientById(patientId).ifPresent(patient ->
                model.addAttribute("patientName", patient.getFirstName() + " " + patient.getLastName()));
        return "prescriptions";
    }

    // ========== MEDICINE MANAGEMENT ==========
    @GetMapping("/medicines")
    public String listMedicines(Model model) {
        model.addAttribute("medicines", medicineRepository.findAll());
        return "medicines";
    }
    @GetMapping("/bills/generate")
    public String showGenerateBillForm(Model model) {
        model.addAttribute("bill", new Bill());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "bill-form";
    }

    @PostMapping("/bills/save")
    public String generateBill(@ModelAttribute Bill bill) {
        billService.generateBill(bill);
        return "redirect:/bills";
    }
}
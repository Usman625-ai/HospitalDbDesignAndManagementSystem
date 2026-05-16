package HospitalManagementSystem.SpringProject.record;

public interface DoctorRecord {
    Long getId();                    // matches "id"
    String getName();                // matches "name"
    String getSpecialization();      // matches "specialization"
    String getDepartmentName();      // matches "departmentName"
    String getStatus();              // matches "status"
    String getEmail();               // matches "email"
    String getRegistrationNumber();  // matches "registrationNumber"
}
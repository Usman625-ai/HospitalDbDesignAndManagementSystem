package HospitalManagementSystem.SpringProject.service;

import HospitalManagementSystem.SpringProject.entity.Investigation;
import HospitalManagementSystem.SpringProject.entity.Status.InvestigationStatus;

import java.util.List;
import java.util.Optional;

public interface InvestigationService {
    // Create
    Investigation requestInvestigation(Investigation investigation);

    // Read
    Optional<Investigation> getInvestigationById(Long id);

    List<Investigation> getAllInvestigations();

    List<Investigation> getInvestigationsByPatient(Long patientId);

    List<Investigation> getPendingInvestigations(Long patientId);

    // Update
    Investigation updateInvestigation(Long id, Investigation investigationDetails);

    Investigation addResults(Long id, String results, String reportUrl);

    Investigation updateStatus(Long id, InvestigationStatus status);

    // Delete
    void deleteInvestigation(Long id);

    // Business methods
    long getPendingCount();

}

package com.garagegenie.service;

import com.garagegenie.dto.JobCardDTOs;
import com.garagegenie.entity.*;
import com.garagegenie.exception.ResourceNotFoundException;
import com.garagegenie.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobCardService {

    private final JobCardRepository jobCardRepo;
    private final UserRepository userRepo;
    private final VehicleRepository vehicleRepo;
    private final EmployeeRepository employeeRepo;
    private final PartRepository partRepo;
    private final AppointmentRepository appointmentRepo;

    public JobCardDTOs.JobCardResponse create(JobCardDTOs.CreateRequest req) {
        User customer = userRepo.findById(req.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", req.getCustomerId()));
        Vehicle vehicle = vehicleRepo.findById(req.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", req.getVehicleId()));

        JobCard.JobCardBuilder builder = JobCard.builder()
                .customer(customer)
                .vehicle(vehicle)
                .problemDescription(req.getProblemDescription())
                .estimatedCost(req.getEstimatedCost())
                .status(Appointment.ServiceStatus.INSPECTION);

        if (req.getAppointmentId() != null) {
            appointmentRepo.findById(req.getAppointmentId()).ifPresent(builder::appointment);
        }
        if (req.getAssignedMechanicId() != null) {
            employeeRepo.findById(req.getAssignedMechanicId()).ifPresent(builder::assignedMechanic);
        }

        return toResponse(jobCardRepo.save(builder.build()));
    }

    public List<JobCardDTOs.JobCardResponse> getAll() {
        return jobCardRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<JobCardDTOs.JobCardResponse> getForMechanic(Long mechanicId) {
        return jobCardRepo.findByAssignedMechanicId(mechanicId).stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobCardDTOs.JobCardResponse> getForCustomer(Long customerId) {
        return jobCardRepo.findByCustomerId(customerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public JobCardDTOs.JobCardResponse getById(Long id) {
        return toResponse(jobCardRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobCard", id)));
    }

    public JobCardDTOs.JobCardResponse updateStatus(Long id, String status) {
        JobCard jobCard = jobCardRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobCard", id));
        jobCard.setStatus(Appointment.ServiceStatus.valueOf(status.toUpperCase()));
        return toResponse(jobCardRepo.save(jobCard));
    }

    public JobCardDTOs.JobCardResponse addNote(Long id, String note) {
        JobCard jobCard = jobCardRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobCard", id));
        JobCardNote jNote = JobCardNote.builder().jobCard(jobCard).note(note).build();
        jobCard.getNotes().add(jNote);
        return toResponse(jobCardRepo.save(jobCard));
    }

    public JobCardDTOs.JobCardResponse addPart(Long id, Long partId, Integer quantity) {
        JobCard jobCard = jobCardRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobCard", id));
        Part part = partRepo.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Part", partId));

        JobCardPart jcp = JobCardPart.builder()
                .jobCard(jobCard)
                .part(part)
                .quantity(quantity)
                .unitPrice(part.getPrice())
                .build();
        jobCard.getPartsUsed().add(jcp);

        // Recalculate estimated cost
        BigDecimal partsCost = jobCard.getPartsUsed().stream()
                .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        jobCard.setEstimatedCost(partsCost);

        // Deduct from stock
        part.setQuantity(part.getQuantity() - quantity);
        partRepo.save(part);

        return toResponse(jobCardRepo.save(jobCard));
    }

    private JobCardDTOs.JobCardResponse toResponse(JobCard j) {
        JobCardDTOs.JobCardResponse r = new JobCardDTOs.JobCardResponse();
        r.setId(j.getId());
        r.setCustomerId(j.getCustomer().getId());
        r.setCustomerName(j.getCustomer().getName());
        r.setVehicleNumber(j.getVehicle().getVehicleNumber());
        r.setEstimatedCost(j.getEstimatedCost());
        r.setActualCost(j.getActualCost());
        r.setStatus(j.getStatus().name());
        r.setCreatedAt(j.getCreatedAt() != null ? j.getCreatedAt().toString() : null);

        if (j.getAssignedMechanic() != null) {
            r.setAssignedMechanicId(j.getAssignedMechanic().getId());
            r.setAssignedMechanicName(j.getAssignedMechanic().getUser().getName());
        }

        r.setNotes(j.getNotes().stream().map(JobCardNote::getNote).collect(Collectors.toList()));
        r.setPartsRequired(j.getPartsUsed().stream().map(p -> {
            JobCardDTOs.PartUsageDTO dto = new JobCardDTOs.PartUsageDTO();
            dto.setPartId(p.getPart().getId());
            dto.setPartName(p.getPart().getName());
            dto.setQuantity(p.getQuantity());
            dto.setUnitPrice(p.getUnitPrice());
            return dto;
        }).collect(Collectors.toList()));

        return r;
    }
}

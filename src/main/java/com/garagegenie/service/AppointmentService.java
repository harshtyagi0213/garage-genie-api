package com.garagegenie.service;

import com.garagegenie.dto.AppointmentDTOs;
import com.garagegenie.entity.Appointment;
import com.garagegenie.entity.User;
import com.garagegenie.entity.Vehicle;
import com.garagegenie.exception.ResourceNotFoundException;
import com.garagegenie.repository.AppointmentRepository;
import com.garagegenie.repository.UserRepository;
import com.garagegenie.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final UserRepository userRepo;
    private final VehicleRepository vehicleRepo;

    public AppointmentDTOs.AppointmentResponse createAppointment(Long customerId, AppointmentDTOs.CreateRequest req) {
        User customer = userRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", customerId));
        Vehicle vehicle = vehicleRepo.findById(req.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", req.getVehicleId()));

        Appointment appointment = Appointment.builder()
                .customer(customer)
                .vehicle(vehicle)
                .serviceType(req.getServiceType())
                .appointmentDate(LocalDate.parse(req.getAppointmentDate()))
                .appointmentTime(LocalTime.parse(req.getAppointmentTime()))
                .notes(req.getNotes())
                .status(Appointment.ServiceStatus.BOOKED)
                .build();

        return toResponse(appointmentRepo.save(appointment));
    }

    public List<AppointmentDTOs.AppointmentResponse> getAllAppointments() {
        return appointmentRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<AppointmentDTOs.AppointmentResponse> getAppointmentsForCustomer(Long customerId) {
        return appointmentRepo.findByCustomerId(customerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public AppointmentDTOs.AppointmentResponse getAppointmentById(Long id) {
        return toResponse(appointmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id)));
    }

    public AppointmentDTOs.AppointmentResponse updateStatus(Long id, String status) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));
        appointment.setStatus(Appointment.ServiceStatus.valueOf(status.toUpperCase()));
        return toResponse(appointmentRepo.save(appointment));
    }

    public void deleteAppointment(Long id) {
        if (!appointmentRepo.existsById(id))
            throw new ResourceNotFoundException("Appointment", id);
        appointmentRepo.deleteById(id);
    }

    private AppointmentDTOs.AppointmentResponse toResponse(Appointment a) {
        AppointmentDTOs.AppointmentResponse r = new AppointmentDTOs.AppointmentResponse();
        r.setId(a.getId());
        r.setCustomerId(a.getCustomer().getId());
        r.setCustomerName(a.getCustomer().getName());
        r.setVehicleId(a.getVehicle().getId());
        r.setVehicleNumber(a.getVehicle().getVehicleNumber());
        r.setVehicleBrand(a.getVehicle().getBrand());
        r.setVehicleModel(a.getVehicle().getModel());
        r.setServiceType(a.getServiceType());
        r.setAppointmentDate(a.getAppointmentDate().toString());
        r.setAppointmentTime(a.getAppointmentTime().toString());
        r.setStatus(a.getStatus().name());
        r.setNotes(a.getNotes());
        return r;
    }
}

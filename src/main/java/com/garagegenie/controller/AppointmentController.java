package com.garagegenie.controller;

import com.garagegenie.dto.AppointmentDTOs;
import com.garagegenie.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Admin: all appointments
    @GetMapping("/api/admin/appointments")
    public ResponseEntity<List<AppointmentDTOs.AppointmentResponse>> getAll() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/api/admin/appointments/{id}")
    public ResponseEntity<AppointmentDTOs.AppointmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PostMapping("/api/admin/appointments")
    public ResponseEntity<AppointmentDTOs.AppointmentResponse> create(
            @RequestParam Long customerId,
            @Valid @RequestBody AppointmentDTOs.CreateRequest req) {
        return ResponseEntity.ok(appointmentService.createAppointment(customerId, req));
    }

    @PatchMapping("/api/admin/appointments/{id}/status")
    public ResponseEntity<AppointmentDTOs.AppointmentResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody AppointmentDTOs.StatusUpdateRequest req) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, req.getStatus()));
    }

    @DeleteMapping("/api/admin/appointments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    // Customer: own appointments
    @GetMapping("/api/customer/appointments")
    public ResponseEntity<List<AppointmentDTOs.AppointmentResponse>> getMyAppointments(@RequestParam Long customerId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsForCustomer(customerId));
    }

    @PostMapping("/api/customer/appointments")
    public ResponseEntity<AppointmentDTOs.AppointmentResponse> bookAppointment(
            @RequestParam Long customerId,
            @Valid @RequestBody AppointmentDTOs.CreateRequest req) {
        return ResponseEntity.ok(appointmentService.createAppointment(customerId, req));
    }
}

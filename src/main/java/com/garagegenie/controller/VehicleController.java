package com.garagegenie.controller;

import com.garagegenie.dto.VehicleDTOs;
import com.garagegenie.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/api/admin/vehicles")
    public ResponseEntity<List<VehicleDTOs.VehicleResponse>> getAll() {
        return ResponseEntity.ok(vehicleService.getAll());
    }

    @GetMapping("/api/admin/vehicles/{id}")
    public ResponseEntity<VehicleDTOs.VehicleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @PostMapping("/api/admin/vehicles")
    public ResponseEntity<VehicleDTOs.VehicleResponse> create(@Valid @RequestBody VehicleDTOs.CreateRequest req) {
        return ResponseEntity.ok(vehicleService.create(req));
    }

    @DeleteMapping("/api/admin/vehicles/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Customer can view their own vehicles
    @GetMapping("/api/customer/vehicles")
    public ResponseEntity<List<VehicleDTOs.VehicleResponse>> getMyVehicles(@RequestParam Long ownerId) {
        return ResponseEntity.ok(vehicleService.getByOwner(ownerId));
    }
}

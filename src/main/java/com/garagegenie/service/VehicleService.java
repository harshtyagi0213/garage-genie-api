package com.garagegenie.service;

import com.garagegenie.dto.VehicleDTOs;
import com.garagegenie.entity.User;
import com.garagegenie.entity.Vehicle;
import com.garagegenie.exception.BadRequestException;
import com.garagegenie.exception.ResourceNotFoundException;
import com.garagegenie.repository.UserRepository;
import com.garagegenie.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepo;
    private final UserRepository userRepo;

    public VehicleDTOs.VehicleResponse create(VehicleDTOs.CreateRequest req) {
        if (vehicleRepo.existsByVehicleNumber(req.getVehicleNumber())) {
            throw new BadRequestException("Vehicle number already registered: " + req.getVehicleNumber());
        }
        User owner = userRepo.findById(req.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", req.getOwnerId()));

        Vehicle vehicle = Vehicle.builder()
                .owner(owner)
                .vehicleNumber(req.getVehicleNumber().toUpperCase())
                .brand(req.getBrand())
                .model(req.getModel())
                .year(req.getYear())
                .type(req.getType() != null ? Vehicle.VehicleType.valueOf(req.getType().toUpperCase()) : null)
                .color(req.getColor())
                .build();

        return toResponse(vehicleRepo.save(vehicle));
    }

    public List<VehicleDTOs.VehicleResponse> getAll() {
        return vehicleRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<VehicleDTOs.VehicleResponse> getByOwner(Long ownerId) {
        return vehicleRepo.findByOwnerId(ownerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public VehicleDTOs.VehicleResponse getById(Long id) {
        return toResponse(vehicleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id)));
    }

    public void delete(Long id) {
        if (!vehicleRepo.existsById(id))
            throw new ResourceNotFoundException("Vehicle", id);
        vehicleRepo.deleteById(id);
    }

    private VehicleDTOs.VehicleResponse toResponse(Vehicle v) {
        VehicleDTOs.VehicleResponse r = new VehicleDTOs.VehicleResponse();
        r.setId(v.getId());
        r.setOwnerId(v.getOwner().getId());
        r.setOwnerName(v.getOwner().getName());
        r.setVehicleNumber(v.getVehicleNumber());
        r.setBrand(v.getBrand());
        r.setModel(v.getModel());
        r.setYear(v.getYear());
        r.setType(v.getType() != null ? v.getType().name() : null);
        r.setColor(v.getColor());
        return r;
    }
}

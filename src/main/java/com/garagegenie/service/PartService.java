package com.garagegenie.service;

import com.garagegenie.dto.PartDTOs;
import com.garagegenie.entity.Part;
import com.garagegenie.exception.ResourceNotFoundException;
import com.garagegenie.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PartService {

    private final PartRepository partRepo;

    public PartDTOs.PartResponse create(PartDTOs.CreateRequest req) {
        Part part = Part.builder()
                .name(req.getName())
                .category(req.getCategory())
                .quantity(req.getQuantity())
                .price(req.getPrice())
                .supplier(req.getSupplier())
                .lowStockThreshold(req.getLowStockThreshold())
                .build();
        return toResponse(partRepo.save(part));
    }

    public List<PartDTOs.PartResponse> getAll() {
        return partRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<PartDTOs.PartResponse> getLowStock() {
        return partRepo.findLowStockParts().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PartDTOs.PartResponse getById(Long id) {
        return toResponse(partRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Part", id)));
    }

    public PartDTOs.PartResponse updateStock(Long id, Integer delta) {
        Part part = partRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Part", id));
        part.setQuantity(part.getQuantity() + delta);
        return toResponse(partRepo.save(part));
    }

    public PartDTOs.PartResponse update(Long id, PartDTOs.CreateRequest req) {
        Part part = partRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Part", id));
        part.setName(req.getName());
        part.setCategory(req.getCategory());
        part.setPrice(req.getPrice());
        part.setSupplier(req.getSupplier());
        part.setLowStockThreshold(req.getLowStockThreshold());
        return toResponse(partRepo.save(part));
    }

    public void delete(Long id) {
        if (!partRepo.existsById(id))
            throw new ResourceNotFoundException("Part", id);
        partRepo.deleteById(id);
    }

    private PartDTOs.PartResponse toResponse(Part p) {
        PartDTOs.PartResponse r = new PartDTOs.PartResponse();
        r.setId(p.getId());
        r.setName(p.getName());
        r.setCategory(p.getCategory());
        r.setQuantity(p.getQuantity());
        r.setPrice(p.getPrice());
        r.setSupplier(p.getSupplier());
        r.setLowStockThreshold(p.getLowStockThreshold());
        return r;
    }
}

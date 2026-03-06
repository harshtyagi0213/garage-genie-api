package com.garagegenie.controller;

import com.garagegenie.dto.PartDTOs;
import com.garagegenie.service.PartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final PartService partService;

    @GetMapping
    public ResponseEntity<List<PartDTOs.PartResponse>> getAll() {
        return ResponseEntity.ok(partService.getAll());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<PartDTOs.PartResponse>> getLowStock() {
        return ResponseEntity.ok(partService.getLowStock());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartDTOs.PartResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(partService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PartDTOs.PartResponse> create(@Valid @RequestBody PartDTOs.CreateRequest req) {
        return ResponseEntity.ok(partService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartDTOs.PartResponse> update(@PathVariable Long id,
            @Valid @RequestBody PartDTOs.CreateRequest req) {
        return ResponseEntity.ok(partService.update(id, req));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<PartDTOs.PartResponse> updateStock(
            @PathVariable Long id, @RequestBody PartDTOs.UpdateStockRequest req) {
        return ResponseEntity.ok(partService.updateStock(id, req.getQuantityDelta()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        partService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

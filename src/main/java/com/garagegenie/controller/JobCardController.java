package com.garagegenie.controller;

import com.garagegenie.dto.JobCardDTOs;
import com.garagegenie.service.JobCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobCardController {

    private final JobCardService jobCardService;

    // Admin endpoints
    @GetMapping("/api/admin/job-cards")
    public ResponseEntity<List<JobCardDTOs.JobCardResponse>> getAll() {
        return ResponseEntity.ok(jobCardService.getAll());
    }

    @GetMapping("/api/admin/job-cards/{id}")
    public ResponseEntity<JobCardDTOs.JobCardResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobCardService.getById(id));
    }

    @PostMapping("/api/admin/job-cards")
    public ResponseEntity<JobCardDTOs.JobCardResponse> create(@Valid @RequestBody JobCardDTOs.CreateRequest req) {
        return ResponseEntity.ok(jobCardService.create(req));
    }

    @PatchMapping("/api/admin/job-cards/{id}/status")
    public ResponseEntity<JobCardDTOs.JobCardResponse> updateStatus(
            @PathVariable Long id, @RequestBody JobCardDTOs.StatusUpdateRequest req) {
        return ResponseEntity.ok(jobCardService.updateStatus(id, req.getStatus()));
    }

    @PostMapping("/api/admin/job-cards/{id}/notes")
    public ResponseEntity<JobCardDTOs.JobCardResponse> addNote(
            @PathVariable Long id, @RequestBody JobCardDTOs.AddNoteRequest req) {
        return ResponseEntity.ok(jobCardService.addNote(id, req.getNote()));
    }

    @PostMapping("/api/admin/job-cards/{id}/parts")
    public ResponseEntity<JobCardDTOs.JobCardResponse> addPart(
            @PathVariable Long id, @RequestBody JobCardDTOs.AddPartRequest req) {
        return ResponseEntity.ok(jobCardService.addPart(id, req.getPartId(), req.getQuantity()));
    }

    // Employee endpoints
    @GetMapping("/api/employee/job-cards")
    public ResponseEntity<List<JobCardDTOs.JobCardResponse>> getForMechanic(@RequestParam Long mechanicId) {
        return ResponseEntity.ok(jobCardService.getForMechanic(mechanicId));
    }

    @PatchMapping("/api/employee/job-cards/{id}/status")
    public ResponseEntity<JobCardDTOs.JobCardResponse> updateStatusEmployee(
            @PathVariable Long id, @RequestBody JobCardDTOs.StatusUpdateRequest req) {
        return ResponseEntity.ok(jobCardService.updateStatus(id, req.getStatus()));
    }

    @PostMapping("/api/employee/job-cards/{id}/notes")
    public ResponseEntity<JobCardDTOs.JobCardResponse> addNoteEmployee(
            @PathVariable Long id, @RequestBody JobCardDTOs.AddNoteRequest req) {
        return ResponseEntity.ok(jobCardService.addNote(id, req.getNote()));
    }
}

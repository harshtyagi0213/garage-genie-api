package com.garagegenie.controller;

import com.garagegenie.dto.InvoiceDTOs;
import com.garagegenie.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/api/admin/invoices")
    public ResponseEntity<List<InvoiceDTOs.InvoiceResponse>> getAll() {
        return ResponseEntity.ok(invoiceService.getAll());
    }

    @GetMapping("/api/admin/invoices/{id}")
    public ResponseEntity<InvoiceDTOs.InvoiceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getById(id));
    }

    @PostMapping("/api/admin/invoices")
    public ResponseEntity<InvoiceDTOs.InvoiceResponse> generate(@Valid @RequestBody InvoiceDTOs.CreateRequest req) {
        return ResponseEntity.ok(invoiceService.generate(req));
    }

    @PatchMapping("/api/admin/invoices/{id}/pay")
    public ResponseEntity<InvoiceDTOs.InvoiceResponse> markPaid(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.markPaid(id));
    }

    @GetMapping("/api/customer/invoices")
    public ResponseEntity<List<InvoiceDTOs.InvoiceResponse>> getMyInvoices(@RequestParam Long customerId) {
        return ResponseEntity.ok(invoiceService.getForCustomer(customerId));
    }
}

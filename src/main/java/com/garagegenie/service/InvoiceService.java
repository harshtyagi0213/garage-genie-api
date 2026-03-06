package com.garagegenie.service;

import com.garagegenie.dto.InvoiceDTOs;
import com.garagegenie.entity.*;
import com.garagegenie.exception.BadRequestException;
import com.garagegenie.exception.ResourceNotFoundException;
import com.garagegenie.repository.InvoiceRepository;
import com.garagegenie.repository.JobCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepo;
    private final JobCardRepository jobCardRepo;

    public InvoiceDTOs.InvoiceResponse generate(InvoiceDTOs.CreateRequest req) {
        JobCard jobCard = jobCardRepo.findById(req.getJobCardId())
                .orElseThrow(() -> new ResourceNotFoundException("JobCard", req.getJobCardId()));

        if (invoiceRepo.findByJobCardId(jobCard.getId()).isPresent()) {
            throw new BadRequestException("Invoice already exists for Job Card: " + jobCard.getId());
        }

        // Calculate parts cost from job card parts
        BigDecimal partsCost = jobCard.getPartsUsed().stream()
                .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal labour = req.getLabourCharges() != null ? req.getLabourCharges() : BigDecimal.ZERO;
        BigDecimal gstPct = req.getGstPercent() != null ? req.getGstPercent() : new BigDecimal("18");

        BigDecimal subtotal = partsCost.add(labour);
        BigDecimal gstAmount = subtotal.multiply(gstPct).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(gstAmount);

        Invoice invoice = Invoice.builder()
                .jobCard(jobCard)
                .customer(jobCard.getCustomer())
                .partsCost(partsCost)
                .labourCharges(labour)
                .gstPercent(gstPct)
                .gstAmount(gstAmount)
                .totalAmount(total)
                .status(Invoice.PaymentStatus.PENDING)
                .build();

        return toResponse(invoiceRepo.save(invoice));
    }

    public InvoiceDTOs.InvoiceResponse markPaid(Long id) {
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", id));
        invoice.setStatus(Invoice.PaymentStatus.PAID);
        return toResponse(invoiceRepo.save(invoice));
    }

    public List<InvoiceDTOs.InvoiceResponse> getAll() {
        return invoiceRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<InvoiceDTOs.InvoiceResponse> getForCustomer(Long customerId) {
        return invoiceRepo.findByCustomerId(customerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public InvoiceDTOs.InvoiceResponse getById(Long id) {
        return toResponse(invoiceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", id)));
    }

    private InvoiceDTOs.InvoiceResponse toResponse(Invoice i) {
        InvoiceDTOs.InvoiceResponse r = new InvoiceDTOs.InvoiceResponse();
        r.setId(i.getId());
        r.setJobCardId(i.getJobCard().getId());
        r.setCustomerId(i.getCustomer().getId());
        r.setCustomerName(i.getCustomer().getName());
        r.setVehicleNumber(i.getJobCard().getVehicle().getVehicleNumber());
        r.setPartsCost(i.getPartsCost());
        r.setLabourCharges(i.getLabourCharges());
        r.setGstPercent(i.getGstPercent());
        r.setGstAmount(i.getGstAmount());
        r.setTotalAmount(i.getTotalAmount());
        r.setStatus(i.getStatus().name());
        r.setDate(i.getInvoiceDate() != null ? i.getInvoiceDate().toString() : null);
        return r;
    }
}

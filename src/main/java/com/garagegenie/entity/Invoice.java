package com.garagegenie.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_card_id", unique = true, nullable = false)
    private JobCard jobCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(name = "parts_cost", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal partsCost = BigDecimal.ZERO;

    @Column(name = "labour_charges", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal labourCharges = BigDecimal.ZERO;

    @Column(name = "gst_percent", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal gstPercent = new BigDecimal("18.00");

    @Column(name = "gst_amount", precision = 10, scale = 2)
    private BigDecimal gstAmount;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "invoice_date")
    @Builder.Default
    private LocalDate invoiceDate = LocalDate.now();

    public enum PaymentStatus {
        PENDING, PAID
    }
}

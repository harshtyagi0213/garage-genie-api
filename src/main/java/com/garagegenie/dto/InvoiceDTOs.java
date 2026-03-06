package com.garagegenie.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class InvoiceDTOs {

    @Data
    public static class CreateRequest {
        @NotNull
        private Long jobCardId;
        private BigDecimal labourCharges;
        private BigDecimal gstPercent = new BigDecimal("18");
    }

    @Data
    public static class InvoiceResponse {
        private Long id;
        private Long jobCardId;
        private Long customerId;
        private String customerName;
        private String vehicleNumber;
        private BigDecimal partsCost;
        private BigDecimal labourCharges;
        private BigDecimal gstPercent;
        private BigDecimal gstAmount;
        private BigDecimal totalAmount;
        private String status;
        private String date;
    }
}

package com.garagegenie.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class JobCardDTOs {

    @Data
    public static class CreateRequest {
        private Long appointmentId;
        @NotNull
        private Long vehicleId;
        @NotNull
        private Long customerId;
        private Long assignedMechanicId;
        @NotBlank
        private String problemDescription;
        private BigDecimal estimatedCost;
    }

    @Data
    public static class AddNoteRequest {
        @NotBlank
        private String note;
    }

    @Data
    public static class StatusUpdateRequest {
        @NotBlank
        private String status;
    }

    @Data
    public static class AddPartRequest {
        @NotNull
        private Long partId;
        @NotNull
        private Integer quantity;
    }

    @Data
    public static class JobCardResponse {
        private Long id;
        private Long customerId;
        private String customerName;
        private String vehicleNumber;
        private Long assignedMechanicId;
        private String assignedMechanicName;
        private String problemDescription;
        private BigDecimal estimatedCost;
        private BigDecimal actualCost;
        private String status;
        private List<String> notes;
        private List<PartUsageDTO> partsRequired;
        private String createdAt;
    }

    @Data
    public static class PartUsageDTO {
        private Long partId;
        private String partName;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}

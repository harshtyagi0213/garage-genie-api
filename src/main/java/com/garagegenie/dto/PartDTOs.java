package com.garagegenie.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PartDTOs {

    @Data
    public static class CreateRequest {
        @NotBlank
        private String name;
        private String category;
        private Integer quantity = 0;
        @NotNull
        private BigDecimal price;
        private String supplier;
        private Integer lowStockThreshold = 5;
    }

    @Data
    public static class UpdateStockRequest {
        @NotNull
        private Integer quantityDelta; // positive = add, negative = remove
    }

    @Data
    public static class PartResponse {
        private Long id;
        private String name;
        private String category;
        private Integer quantity;
        private BigDecimal price;
        private String supplier;
        private Integer lowStockThreshold;
    }
}

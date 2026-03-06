package com.garagegenie.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

public class VehicleDTOs {

    @Data
    public static class CreateRequest {
        private Long ownerId;
        @NotBlank
        private String vehicleNumber;
        @NotBlank
        private String brand;
        @NotBlank
        private String model;
        private Integer year;
        private String type;
        private String color;
    }

    @Data
    public static class VehicleResponse {
        private Long id;
        private Long ownerId;
        private String ownerName;
        private String vehicleNumber;
        private String brand;
        private String model;
        private Integer year;
        private String type;
        private String color;
    }
}

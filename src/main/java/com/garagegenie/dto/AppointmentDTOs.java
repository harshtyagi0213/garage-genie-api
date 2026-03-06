package com.garagegenie.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AppointmentDTOs {

    @Data
    public static class CreateRequest {
        @NotNull
        private Long vehicleId;
        @NotBlank
        private String serviceType;
        @NotBlank
        private String appointmentDate; // yyyy-MM-dd
        @NotBlank
        private String appointmentTime; // HH:mm
        private String notes;
    }

    @Data
    public static class AppointmentResponse {
        private Long id;
        private Long customerId;
        private String customerName;
        private Long vehicleId;
        private String vehicleNumber;
        private String vehicleBrand;
        private String vehicleModel;
        private String serviceType;
        private String appointmentDate;
        private String appointmentTime;
        private String status;
        private String notes;
    }

    @Data
    public static class StatusUpdateRequest {
        @NotBlank
        private String status;
    }
}

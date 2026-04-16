package com.healthcare.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentResponse {
    private Long id;
    private Long userId;
    private Long centerId;
    private Long testId;
    private String status;
    private String appointmentDate;
    private String appointmentTime;
    private String userName;
    private String centerName;
    private String testName;
    private Integer testPrice;
}
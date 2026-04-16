package com.healthcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.services.EmailService;
import com.healthcare.services.OtpService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OtpController {
 
    private final OtpService otpService;
    private final EmailService emailService;
 
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
 
        String otp = otpService.generateOtp(email);
        emailService.sendOtp(email, otp);
 
        return ResponseEntity.ok("OTP sent successfully");
    }
 
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {
 
        if (otpService.validateOtp(email, otp)) {
            return ResponseEntity.ok("OTP verified");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
    }
}

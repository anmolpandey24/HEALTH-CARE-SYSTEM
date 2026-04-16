package com.healthcare.services;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
 
    private final JavaMailSender  mailSender;
 

    public void sendOtp(String toEmail, String otp) {
 
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("OTP Verification - Healthcare App");
 
        message.setText(
                "Hello,\n\n" +
                "Your OTP is: " + otp + "\n" +
                "Valid for 5 minutes.\n\n" +
                "Do not share this OTP.\n\n" +
                "Thanks!"
        );
 
        mailSender.send(message);
    }
}

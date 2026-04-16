package com.healthcare.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class OtpService {
 
   
    private final Map<String, Long> expiryMap = new HashMap<>();
    
    private final Map<String, String> otpMap = new HashMap<>();
 
    public String generateOtp(String email) {
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
 
        otpMap.put(email, otp);
        expiryMap.put(email, System.currentTimeMillis() + 5 * 60 * 1000); // 5 minutes

        // For debugging purposes - remove in production
        System.out.println("Generated OTP for " + email + ": " + otp);
 
        return otp;
    }
 
    public boolean validateOtp(String email, String otp) {
        if (!otpMap.containsKey(email)) return false;
 
        if (System.currentTimeMillis() > expiryMap.get(email)) {
            otpMap.remove(email);
            return false;
        }
 
        return otpMap.get(email).equals(otp);
    }
}
 
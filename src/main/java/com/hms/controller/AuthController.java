package com.hms.controller;

import com.hms.entity.Patient;
import com.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Patient patient = patientRepository.findByMobile(loginRequest.getMobile())
            .orElseThrow(() -> new RuntimeException("Patient not found"));

        if ("PATIENT".equals(loginRequest.getRole()) && passwordEncoder.matches(loginRequest.getPassword(), patient.getPassword())) {
            return ResponseEntity.ok(patient);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}

class LoginRequest {
    private String mobile;
    private String password;
    private String role;

    // Getters and setters
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
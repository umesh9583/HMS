package com.hms.controller;

import com.hms.entity.Doctor;
import com.hms.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    // Get all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return ResponseEntity.ok(doctors);
    }

    // Get a doctor by mobile number
    @GetMapping("/mobile/{mobile}")
    public ResponseEntity<Doctor> getDoctorByMobile(@PathVariable String mobile) {
        Doctor doctor = doctorRepository.findByMobile(mobile)
            .orElseThrow(() -> new RuntimeException("Doctor not found with mobile: " + mobile));
        return ResponseEntity.ok(doctor);
    }
}
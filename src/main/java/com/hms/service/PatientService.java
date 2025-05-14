package com.hms.service;

import com.hms.entity.Patient;
import com.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient registerPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient patient) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        existing.setName(patient.getName());
        existing.setGuardianName(patient.getGuardianName());
        existing.setGender(patient.getGender());
        existing.setAddress(patient.getAddress());
        existing.setPhone(patient.getPhone());
        existing.setEmail(patient.getEmail());
        return patientRepository.save(existing);
    }
}
package com.hms.controller;

import com.hms.entity.Appointment;
import com.hms.entity.Doctor;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    // Assign a doctor to a patient's appointment
    @PutMapping("/assign-doctor")
    public ResponseEntity<Appointment> assignDoctorToPatient(
            @RequestParam Long appointmentId,
            @RequestParam Long doctorId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

        if (!appointmentOpt.isPresent()) {
            return ResponseEntity.badRequest().body(null); // Appointment not found
        }
        if (!doctorOpt.isPresent()) {
            return ResponseEntity.badRequest().body(null); // Doctor not found
        }

        Appointment appointment = appointmentOpt.get();
        appointment.setDoctor(doctorOpt.get());
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return ResponseEntity.ok(updatedAppointment);
    }
}
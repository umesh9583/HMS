package com.hms.service;

import com.hms.entity.Appointment;
import com.hms.entity.Doctor;
import com.hms.entity.Patient;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    // Create a new appointment
    public Appointment createAppointment(Appointment appointment) {
        // Validate patient and doctor existence
        Optional<Patient> patient = patientRepository.findById(appointment.getPatient().getId());
        if (!patient.isPresent()) {
            throw new IllegalArgumentException("Patient with ID " + appointment.getPatient().getId() + " not found.");
        }
        Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctor().getId());
        if (!doctor.isPresent()) {
            throw new IllegalArgumentException("Doctor with ID " + appointment.getDoctor().getId() + " not found.");
        }

        // Set patient and doctor
        appointment.setPatient(patient.get());
        appointment.setDoctor(doctor.get());

        // Validate appointment date (e.g., must be in the future)
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date must be in the future.");
        }

        // Check for doctor availability (simplified check)
        List<Appointment> existingAppointments = appointmentRepository.findAll();
        boolean isDoctorBusy = existingAppointments.stream()
                .filter(a -> a.getDoctor().getId().equals(appointment.getDoctor().getId()))
                .anyMatch(a -> a.getAppointmentDate().equals(appointment.getAppointmentDate()));
        if (isDoctorBusy) {
            throw new IllegalStateException("Doctor is already booked at this time.");
        }

        // Set default status if not provided
        if (appointment.getStatus() == null) {
            appointment.setStatus("Scheduled");
        }

        return appointmentRepository.save(appointment);
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get appointment by ID
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    // Update an appointment
    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        // Check if the appointment exists
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
        if (!existingAppointment.isPresent()) {
            throw new IllegalArgumentException("Appointment with ID " + id + " not found.");
        }

        Appointment appointment = existingAppointment.get();

        // Validate and update patient
        if (updatedAppointment.getPatient() != null && updatedAppointment.getPatient().getId() != null) {
            Optional<Patient> patient = patientRepository.findById(updatedAppointment.getPatient().getId());
            if (!patient.isPresent()) {
                throw new IllegalArgumentException("Patient with ID " + updatedAppointment.getPatient().getId() + " not found.");
            }
            appointment.setPatient(patient.get());
        }

        // Validate and update doctor
        if (updatedAppointment.getDoctor() != null && updatedAppointment.getDoctor().getId() != null) {
            Optional<Doctor> doctor = doctorRepository.findById(updatedAppointment.getDoctor().getId());
            if (!doctor.isPresent()) {
                throw new IllegalArgumentException("Doctor with ID " + updatedAppointment.getDoctor().getId() + " not found.");
            }
            appointment.setDoctor(doctor.get());
        }

        // Update appointment date if provided
        if (updatedAppointment.getAppointmentDate() != null) {
            if (updatedAppointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Appointment date must be in the future.");
            }
            appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
        }

        // Update status if provided, with validation
        if (updatedAppointment.getStatus() != null) {
            String status = updatedAppointment.getStatus().trim();
            if (!status.equals("Scheduled") && !status.equals("Completed") && !status.equals("Canceled")) {
                throw new IllegalArgumentException("Invalid status. Must be 'Scheduled', 'Completed', or 'Canceled'.");
            }
            appointment.setStatus(status);
        }

        // Save the updated appointment
        return appointmentRepository.save(appointment);
    }

    // Delete an appointment
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Appointment with ID " + id + " not found.");
        }
        appointmentRepository.deleteById(id);
    }

    // Get appointment stats for dashboard
    public Map<String, Integer> getAppointmentStats() {
        List<Appointment> appointments = appointmentRepository.findAll();
        LocalDate today = LocalDate.now();
        long totalAppointments = appointments.size();
        long todayAppointments = appointments.stream()
                .filter(a -> a.getAppointmentDate().toLocalDate().isEqual(today))
                .count();

        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalAppointments", (int) totalAppointments);
        stats.put("todayAppointments", (int) todayAppointments);
        return stats;
    }
}
package com.hms.service;

   import com.hms.entity.Doctor;
   import com.hms.repository.DoctorRepository;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Service;

   import java.util.List;

   @Service
   public class DoctorService {
       @Autowired
       private DoctorRepository doctorRepository;

       public Doctor saveDoctor(Doctor doctor) {
           return doctorRepository.save(doctor);
       }

       public List<Doctor> getAllDoctors() {
           return doctorRepository.findAll();
       }

       public Doctor getDoctorById(Long id) {
           return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
       }
   }
package com.hms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String guardianName;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private String aadharNumber;
    private String mobile;
    private String password;
    private String role;
    private Integer age;
    private String bloodGroup;
}
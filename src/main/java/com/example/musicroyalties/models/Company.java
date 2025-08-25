package com.example.musicroyalties.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "companies")
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String companyName;
    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String contactPerson;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

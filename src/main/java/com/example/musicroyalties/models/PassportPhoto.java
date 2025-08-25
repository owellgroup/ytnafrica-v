package com.example.musicroyalties.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "passport_photos")
public class PassportPhoto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String imageTitle;
    private String imageUrl;
    private String fileType;
    private LocalDate datePosted;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

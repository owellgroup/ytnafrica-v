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
@Table(name = "artist_work")
public class ArtistWork {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titleOfWork;
    
    @Column(nullable = false)
    private String fileUrl;
    
    @Column(nullable = false)
    private String fileType;
    
    private LocalDate uploadedDate;
    
    @ManyToOne
    @JoinColumn(name = "artist_upload_type_id")
    private ArtistUploadType artistUploadType;
    
    @ManyToOne
    @JoinColumn(name = "artist_work_type_id")
    private ArtistWorkType artistWorkType;
    
    @ManyToOne
    @JoinColumn(name = "license_by_id")
    private LicenseBy licenseBy;
    //Additional Details
    private String Duration;

    
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;
    
    private String ISRC_code;
    private String notes;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

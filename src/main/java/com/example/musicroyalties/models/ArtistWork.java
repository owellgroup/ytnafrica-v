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

    //
    private String ArtistId;
    
    @Column(nullable = false)
    private String title;
    private String albumName;
    private String artist;
    private String GroupOrBandOrStageName;
    private String featuredArtist;
    private String producer;

    //CustomIds for works
    @Column(unique = true)
    private String workId;

    private String Duration;
    
    @Column(nullable = false)
    private String fileUrl;
    
    @Column(nullable = false)
    private String fileType;
    private String country;
    
    private LocalDate uploadedDate;
    
    @ManyToOne
    @JoinColumn(name = "artist_upload_type_id")
    private ArtistUploadType artistUploadType;//video or mp3
    
    @ManyToOne
    @JoinColumn(name = "artist_work_type_id")
    private ArtistWorkType artistWorkType;//Pop//jazz etc




    @ManyToOne()
    @JoinColumn(name = "status_id")
    private Status status;
    
    private String ISRC_code;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Extra Details
    private String composer;
    private String author;
    private String arranger;
    private String publisher;
    private String publishersName;
    private String publisherAdress;
    private String publisherTelephone;
    private String recordedBy;
    private String AddressOfRecordingCompany;
    private String RecordingCompanyTelephone;
    private String labelName;
    private String dateRecorded;
    //private String ipiNumber;


}

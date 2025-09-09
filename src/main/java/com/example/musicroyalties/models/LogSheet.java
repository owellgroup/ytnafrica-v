package com.example.musicroyalties.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "log_sheets")
public class LogSheet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private LocalDate createdDate;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @ManyToMany
    @JoinTable(
        name = "logsheet_music",
        joinColumns = @JoinColumn(name = "logsheet_id"),
        inverseJoinColumns = @JoinColumn(name = "music_id")
    )
    private List<ArtistWork> selectedMusic;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

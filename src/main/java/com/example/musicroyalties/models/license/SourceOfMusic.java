package com.example.musicroyalties.models.license;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sourceofmusic")
public class SourceOfMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String SourceOfMusic;//e.g Radio, Pre-recorded, Tv Audio, Other
}

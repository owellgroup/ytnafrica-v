package com.example.musicroyalties.models.license;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="musicusagetypes")
public class MusicUsageTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id;
    private String UsageType;//e.g sport, live events, perfomance, bars etc, buses, fitness
}

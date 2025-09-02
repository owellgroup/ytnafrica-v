package com.example.musicroyalties.models.license;

import com.example.musicroyalties.models.Tittle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="legalEntity")
public class LegalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String CompanyName;
    private String CompanyShortName;//e.g NBC
    private String RegistrationNumber;
    private String VATStatus;
    private String VATNumber;
    //owners Contact information
    private String OwnerFirstName;
    private String OwnerLastName;
    private String OwnerEmail;
    private String OwnerPhone;
    @ManyToOne
    @JoinColumn(name="title_id")
    private Tittle OwnerTitle;
    private int NumberOfPremises;
    private String BuildingName;
    private String UnitNoOrShop;
    private String Street;
    private String Suburb;
    private String CityOrTown;
    private String Country;
    private String PostalCode;
    @ManyToOne
    @JoinColumn(name="musicusage_id")
    private MusicUsageTypes musicUsageType;
    @ManyToOne
    @JoinColumn(name="sourceofmusic_id")
    private SourceOfMusic sourceOfMusic;

}

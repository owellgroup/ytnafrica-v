package com.example.musicroyalties.models.license;

import com.example.musicroyalties.models.Tittle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="NaturalPersonEntity")
public class NaturalPersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Surnam;
    private String FirstName;
    private int IdNumber;
    @ManyToOne
    @JoinColumn(name="tittle_id")
    private Tittle Title;
    private String BusinessRoleOrTittle;
    private String Email;
    private String Phone;
    private String Fax;
    //address
    private String AddressLocation;
    private String UnitNo;
    private String CityOrTown;
    private String Suburb;
    private String Province;
    private String Country;
    private String PostalCode;
    private String Street;
    //Details of the Premises
    private int NumberOfPremises;
    private LocalDate CommencementDate;
    private String TradingNameOfBusiness;
    @ManyToOne
    @JoinColumn(name="musicusage_id")
    private MusicUsageTypes musicUsageType;

    //source of music
    @ManyToOne
    @JoinColumn(name="sourceofmusic")
    private SourceOfMusic sourceOfMusic;

}

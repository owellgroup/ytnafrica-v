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
@Table(name = "member_details")
public class MemberDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="Tittle_id")
    private Tittle tittle;
    @Column(nullable = false)
    private String firstName;

    //tittle

    
    @Column(nullable = false)
    private String surname;

    private int idNumber;

    @Column(unique = true)
    private String ArtistId;
    
    private String pseudonym;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String email;
    
    private String groupNameORStageName;
    
    @ManyToOne
    @JoinColumn(name = "marital_status_id")
    private MaritalStatus maritalStatus;
    
    @ManyToOne
    @JoinColumn(name = "member_category_id")
    private MemberCategory memberCategory;
    
    private Integer noOFDependents;
    
    private String typeOfWork;
    
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;
    //address details
    private String line1;
    private String line2;
    private String city;
    private String region;
    private String poBox;
    private String postalCode;
    private String country;
    
    private LocalDate birthDate;
    private String placeOfBirth;
    private String idOrPassportNumber;
    private String nationality;
    private String occupation;
    private String nameOfEmployer;
    private String addressOfEmployer;
    
    private String nameOfTheBand;
    private LocalDate dateFounded;
    private Integer numberOfBand;
    
    @ManyToOne
    @JoinColumn(name = "bank_name_id")
    private BankName bankName;
    
    private String accountHolderName;
    private String bankAccountNumber;
    private String bankAccountType;
    private String bankBranchName;
    private String bankBranchNumber;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;
    
    private String IPI_number;
    private String notes;
}

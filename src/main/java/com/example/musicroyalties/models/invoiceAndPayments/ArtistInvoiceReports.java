package com.example.musicroyalties.models.invoiceAndPayments;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="artistinvoicesreports")
public class ArtistInvoiceReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentId;

    private String ArtistName;
    private String ArtistPhoneNumber;
    private String ArtistEmail;
    private String ArtistId;
    private String Desciption;
    private String paymentDate;

    // Company (Sender) Details
    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String contactPerson;

    private Double totalplayed;
    private Double UnitPrice;
    private Double TotalEarned;
    private Double TotalNetpaid;

    //Artist Bank Account
    private String BankName;
    private int AccountNumber;
    private String branchName;
    private LocalDate datecreated;

}

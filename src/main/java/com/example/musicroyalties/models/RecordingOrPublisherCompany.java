//THis can wait for future use

package com.example.musicroyalties.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="PublisherOrRecordingCompany")
public class RecordingOrPublisherCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String CompanyId;
    private String NameOfApplican;
    private String TradingName;
    private String CompanyRegistrationNumber;
    private LocalDate DateOfRegistration;
    private String TaxNumber;
    private String VatNumber;
    private String BusinessAddress;
    private String PostalAddress;
    private String TelephoneNumber;
    private String Email;
    private String Website;
    private String NominatedContactPersonName;
    private String NominatedContactPersonPhone;
    //Directors details
    private String DirectorsFirstName;
    private String DirectorsLastName;
    private String DirectorsIdNumber;
    //Banking
    private String BankName;
    private int AccountNumber;
    private String BranchName;
    private String BranchCode;

    //


}

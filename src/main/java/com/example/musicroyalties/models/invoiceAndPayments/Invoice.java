// src/main/java/com/example/musicroyalties/models/invoiceAndPayments/Invoice.java

package com.example.musicroyalties.models.invoiceAndPayments;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Company (Sender) Details
    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String contactPerson;

    // Invoice Info
    private String invoiceNumber;
    private String invoiceDate;

    // Client (Billing To)
    private String billingToCompanyName;
    private String billingToCompanyAddress;
    private String billingToCompanyPhone;
    private String billingToCompanyEmail;

    // Service Details
    private String invoiceServiceType;
    private int totalUsed;
    private Double unitPrice;
    private Double totalAmount;
    private Double totalNetAmount;

    // Bank Details
    private int accountNumber;
    private String bankName;
    private String branchName;
    private LocalDate datecreated;
}
package com.example.musicroyalties.models.invoiceAndPayments;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="invoiceservicetype")
public class InvoiceServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ServiceType;//e.g TV Broadcasting, Radio Broadcasting, Annuall Public Perfomance, Artist Perfomamce at Shows etc

}

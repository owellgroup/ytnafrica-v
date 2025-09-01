package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.invoiceAndPayments.ArtistInvoiceReports;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistInvoiceReportsRepository extends JpaRepository<ArtistInvoiceReports, Long> {
}
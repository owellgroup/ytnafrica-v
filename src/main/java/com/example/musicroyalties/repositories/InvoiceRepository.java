package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.invoiceAndPayments.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
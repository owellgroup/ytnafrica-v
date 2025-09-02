package com.example.musicroyalties.services.invoicesServices;

import com.example.musicroyalties.models.invoiceAndPayments.Invoice;
import com.example.musicroyalties.repositories.InvoiceRepository;
import com.example.musicroyalties.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private EmailService emailService;

    //posting
    public Invoice send(Invoice invoice, String clientEmail) throws Exception {
        String userId = generateUserId(invoice.getBankName(), invoice.getBillingToCompanyName());
        invoice.setInvoiceNumber(userId);
        invoice.setDatecreated(LocalDate.now());
        Invoice savedInvoice = invoiceRepository.save(invoice);
        emailService.sendInvoiceEmail(clientEmail, savedInvoice);
        return savedInvoice;
    }
    //
    private String generateUserId(String bankName, String billingToCompanyName) {
        String prefix = "INV";
        String lastNamePart = bankName.length() >= 2 ? bankName.substring(0, 2).toUpperCase() : bankName.toUpperCase();
        String lastNamePart2 = billingToCompanyName.length() >= 2 ? billingToCompanyName.substring(0, 2).toUpperCase() : billingToCompanyName.toUpperCase();
//        String yearPart = String.valueOf(birthYear).substring(2);
        //method

        // Get the current count of members from the database and add 1
        Long count = invoiceRepository.count() + 1;

        // Format the count with leading zeros (e.g., 001, 002, ..., 1000)
        String counterPart = String.format("%04d", count);


        return prefix + lastNamePart  + lastNamePart2 +  counterPart;
    }

    //get all invoices
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    //find by Id
    public Optional<Invoice> getInvoice(Long id) {
        return invoiceRepository.findById(id);
    }



}

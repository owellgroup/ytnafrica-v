package com.example.musicroyalties.services.invoicesServices;

import com.example.musicroyalties.models.invoiceAndPayments.ArtistInvoiceReports;
import com.example.musicroyalties.repositories.ArtistInvoiceReportsRepository;
import com.example.musicroyalties.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private ArtistInvoiceReportsRepository artistInvoiceReportsRepository;
    @Autowired
    private EmailService emailService;

    //posting
    //posting
    public ArtistInvoiceReports send(ArtistInvoiceReports invoice, String clientEmail) throws Exception {
        String userId = generateUserId(invoice.getBankName(), invoice.getArtistName());
        invoice.setPaymentId(userId);
        invoice.setDatecreated(LocalDate.now());
        ArtistInvoiceReports save = artistInvoiceReportsRepository.save(invoice);
        emailService.sendPayment(clientEmail, save);
        return save;
    }
    //
    private String generateUserId(String BankName, String ArtistName) {
        String prefix = "PAY";
        String lastNamePart = BankName.length() >= 2 ? BankName.substring(0, 2).toUpperCase() : BankName.toUpperCase();
        String lastNamePart2 = ArtistName.length() >= 2 ? ArtistName.substring(0, 2).toUpperCase() : ArtistName.toUpperCase();
//        String yearPart = String.valueOf(birthYear).substring(2);
        //method

        // Get the current count of members from the database and add 1
        Long count = artistInvoiceReportsRepository.count() + 1;

        // Format the count with leading zeros (e.g., 001, 002, ..., 1000)
        String counterPart = String.format("%04d", count);


        return prefix + lastNamePart  + lastNamePart2 +  counterPart;
    }

    //Get All Invoice
    public List<ArtistInvoiceReports> findAll() {
        return artistInvoiceReportsRepository.findAll();
    }

    //get by Id
    public Optional<ArtistInvoiceReports> findById(Long id) {
        return artistInvoiceReportsRepository.findById(id);
    }


}

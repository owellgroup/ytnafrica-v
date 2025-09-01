package com.example.musicroyalties.controllers.invoiceControllers;

import com.example.musicroyalties.models.invoiceAndPayments.ArtistInvoiceReports;
import com.example.musicroyalties.models.invoiceAndPayments.Invoice;
import com.example.musicroyalties.services.EmailService;
import com.example.musicroyalties.services.invoicesServices.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artistpayments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ArtistInvoiceReports sendInvoice(@Valid @RequestBody ArtistInvoiceReports invoice, @RequestParam String clientEmail) throws Exception {
        return paymentService.send(invoice, clientEmail);

    }

    //Get All
    @GetMapping("/all")
    public List<ArtistInvoiceReports> getAllInvoices() {
        return paymentService.findAll();
    }
    //Get By Id
    @GetMapping("/{id}")
    public Optional<ArtistInvoiceReports> getInvoice(@PathVariable long id) {
        return paymentService.findById(id);
    }
}

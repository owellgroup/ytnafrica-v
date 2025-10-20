package com.example.musicroyalties.controllers.invoiceControllers;

import com.example.musicroyalties.models.invoiceAndPayments.Invoice;
import com.example.musicroyalties.services.EmailService;
import com.example.musicroyalties.services.invoicesServices.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")

public class InvoiceController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private InvoiceService invoiceService;


    @PostMapping("/send")
    public Invoice  sendInvoice(@Valid @RequestBody Invoice invoice, @RequestParam String clientEmail) throws Exception {
        return invoiceService.send(invoice, clientEmail);

    }

    //get All the Invoices
    @GetMapping("/all")
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    //Get By Id
    @GetMapping("/{id}")
    public Optional<Invoice> getInvoice(@PathVariable long id) {
        return invoiceService.getInvoice(id);
    }

}

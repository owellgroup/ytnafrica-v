package com.example.musicroyalties.controllers.invoiceControllers;

import com.example.musicroyalties.models.invoiceAndPayments.Invoice;
import com.example.musicroyalties.services.EmailService;
import com.example.musicroyalties.services.invoicesServices.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    /**
     * POST /api/invoices
     * Save a new invoice to the database.
     */
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) {
        // TODO: Save to DB using repository (e.g., invoiceRepository.save(invoice))
        // For now, echoing back
        return ResponseEntity.created(URI.create("/api/invoices/" + 1L)).body(invoice);
    }

    /**
     * GET /api/invoices/{id}
     * Retrieve an invoice by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        // TODO: Fetch from DB
        return ResponseEntity.notFound().build(); // Placeholder
    }

    /**
     * PUT /api/invoices/{id}
     * Update an existing invoice.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @Valid @RequestBody Invoice invoice) {
        // TODO: Update logic
        return ResponseEntity.ok(invoice);
    }

    /**
     * DELETE /api/invoices/{id}
     * Delete an invoice.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        // TODO: Delete from DB
        return ResponseEntity.noContent().build();
    }
}
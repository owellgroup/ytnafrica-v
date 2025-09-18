package com.example.musicroyalties.services.invoicesServices;// src/main/java/com/example/musicroyalties/services/PdfInvoiceGenerator.java



import com.example.musicroyalties.models.invoiceAndPayments.ArtistInvoiceReports;
import com.example.musicroyalties.models.invoiceAndPayments.Invoice;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PdfGenForArtistService {

    private static final String LOGO_URL = "https://namsa.vercel.app/assets/namsa-logo-BGptgL6M.png";

    public byte[] generatePdf(ArtistInvoiceReports artistinvoice) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Set up PDF writer and document
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add Logo and Title
            ImageData imageData = ImageDataFactory.create(LOGO_URL);
            Image logo = new Image(imageData).setWidth(120); // Adjust size as needed
            logo.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            document.add(logo);

            // Invoice Title
            Paragraph title = new Paragraph("Proof of Payments")
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setMarginTop(10f);
            document.add(title);

            // Spacer
            document.add(new Paragraph("\n"));

            // Invoice Info Table (2 columns: From & Bill To)
            Table headerTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            headerTable.setMarginTop(10);

            // FROM (Sender)
            Cell fromCell = new Cell().setBorder(null);
            fromCell.add(new Paragraph("From:").setBold());
            fromCell.add(new Paragraph("YTNAfrica / NASCAM"));
            fromCell.add(new Paragraph(artistinvoice.getCompanyAddress()));
            fromCell.add(new Paragraph("Phone: " + artistinvoice.getCompanyPhone()));
            fromCell.add(new Paragraph("Email: " + artistinvoice.getCompanyEmail()));
            fromCell.add(new Paragraph("Contact: " + artistinvoice.getContactPerson()));
            headerTable.addCell(fromCell);

            // payments TO (Client)
            Cell billToCell = new Cell().setBorder(null);
            billToCell.add(new Paragraph("Payments To:").setBold());
            billToCell.add(new Paragraph(artistinvoice.getArtistName()));
            billToCell.add(new Paragraph(artistinvoice.getArtistPhoneNumber()));
            billToCell.add(new Paragraph("Email: " + artistinvoice.getCompanyEmail()));
            billToCell.add(new Paragraph("ArtistID: " + artistinvoice.getArtistId()));
            headerTable.addCell(billToCell);

            document.add(headerTable);

            // Invoice Details Table (Single row: Number, Date, Service)
            Table infoTable = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
            infoTable.setMarginTop(20);
            infoTable.addHeaderCell(createHeaderCell("Payment Number"));
            infoTable.addHeaderCell(createHeaderCell("Date"));
            infoTable.addHeaderCell(createHeaderCell("Service"));

            infoTable.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(artistinvoice.getPaymentId())));
            infoTable.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(artistinvoice.getPaymentDate())));
            infoTable.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(artistinvoice.getDesciption())));

            document.add(infoTable);

            // Itemized Table (Units, Unit Price, Total, Net)
            Table itemTable = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
            itemTable.setMarginTop(20);

            itemTable.addHeaderCell(createHeaderCell("Description"));
            itemTable.addHeaderCell(createHeaderCell("Quantity"));
            itemTable.addHeaderCell(createHeaderCell("Unit Price (NAD)"));
            itemTable.addHeaderCell(createHeaderCell("Total (NAD)"));

            // Add service row
            itemTable.addCell(new Cell().setTextAlignment(TextAlignment.LEFT).add(new Paragraph(artistinvoice.getDesciption())));
            itemTable.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(artistinvoice.getTotalplayed()))));
            itemTable.addCell(new Cell().setTextAlignment(TextAlignment.RIGHT).add(new Paragraph("N$" + format(artistinvoice.getUnitPrice()))));
            itemTable.addCell(new Cell().setTextAlignment(TextAlignment.RIGHT).add(new Paragraph("N$" + format(artistinvoice.getTotalEarned()))));

            // Net Amount Row
            itemTable.addCell(new Cell(1, 3) // colspan 3
                    .setBorderTop(null)
                    .setBorderLeft(null)
                    .setBorderRight(null)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .add(new Paragraph("Net Amount:").setBold()));
            itemTable.addCell(new Cell()
                    .setBorderTop(null)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .add(new Paragraph("N$" + format(artistinvoice.getTotalNetpaid())).setBold()));

            document.add(itemTable);

            // Payment Instructions
            document.add(new Paragraph("\nPayment Instructions").setBold().setMarginTop(20));
            document.add(new Paragraph("Bank: " + artistinvoice.getBankName()));
            document.add(new Paragraph("Branch: " + artistinvoice.getAccountNumber()));
            document.add(new Paragraph("Account Number: " + artistinvoice.getBranchName()));

            // Terms & Conditions
            document.add(new Paragraph("\nTerms & Conditions").setBold().setMarginTop(20));
            document.add(new Paragraph(
                    "1. This report is provided for informational purposes and reflects transactions recorded up to the report date.\n" +
                            "2. Final payment amounts are subject to contract terms, adjustments, and applicable taxes.\n" +
                            "3. The report is confidential and intended solely for the artist; unauthorized sharing is prohibited.\n" +
                            "4. Any discrepancies must be reported within 14 days of receipt"
            ).setFontSize(10).setFontColor(ColorConstants.GRAY));




            // Close document
            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF invoice", e);
        }
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .add(new Paragraph(text));
    }

    private String format(Double value) {
        if (value == null) return "0.00";
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .toString();
    }

    //invoice number


}

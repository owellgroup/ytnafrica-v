// src/main/java/com/example/musicroyalties/services/PdfInvoiceGenerator.java

package com.example.musicroyalties.services;

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
public class PdfInvoiceGenerator {

    private static final String LOGO_URL = "https://namsa.vercel.app/assets/namsa-logo-BGptgL6M.png";

    public byte[] generatePdf(Invoice invoice) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Set up PDF writer and document
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add Logo and Title
            ImageData imageData = ImageDataFactory.create(LOGO_URL);
            Image logo = new Image(imageData).setWidth(120); // Adjust size as needed
            logo.setHorizontalAlignment(HorizontalAlignment.LEFT);
            document.add(logo);

            // Invoice Title
            Paragraph title = new Paragraph("INVOICE")
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setTextAlignment(TextAlignment.LEFT)
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
            fromCell.add(new Paragraph("NAMSA"));
            fromCell.add(new Paragraph(invoice.getCompanyAddress()));
            fromCell.add(new Paragraph("Phone: " + invoice.getCompanyPhone()));
            fromCell.add(new Paragraph("Email: " + invoice.getCompanyEmail()));
            fromCell.add(new Paragraph("Contact: " + invoice.getContactPerson()));
            headerTable.addCell(fromCell);

            // BILL TO (Client)
            Cell billToCell = new Cell().setBorder(null);
            billToCell.add(new Paragraph("Bill To:").setBold());
            billToCell.add(new Paragraph(invoice.getBillingToCompanyName()));
            billToCell.add(new Paragraph(invoice.getBillingToCompanyAddress()));
            billToCell.add(new Paragraph("Phone: " + invoice.getBillingToCompanyPhone()));
            billToCell.add(new Paragraph("Email: " + invoice.getBillingToCompanyEmail()));
            headerTable.addCell(billToCell);

            document.add(headerTable);

            // Invoice Details Table (Single row: Number, Date, Service)
            Table infoTable = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
            infoTable.setMarginTop(20);
            infoTable.addHeaderCell(createHeaderCell("Invoice Number"));
            infoTable.addHeaderCell(createHeaderCell("Date"));
            infoTable.addHeaderCell(createHeaderCell("Service"));

            infoTable.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(invoice.getInvoiceNumber())));
            infoTable.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(invoice.getInvoiceDate())));
            infoTable.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(invoice.getInvoiceServiceType())));

            document.add(infoTable);

            // Itemized Table (Units, Unit Price, Total, Net)
            Table itemTable = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
            itemTable.setMarginTop(20);

            itemTable.addHeaderCell(createHeaderCell("Description"));
            itemTable.addHeaderCell(createHeaderCell("Quantity"));
            itemTable.addHeaderCell(createHeaderCell("Unit Price (NAD)"));
            itemTable.addHeaderCell(createHeaderCell("Total (NAD)"));

            // Add service row
            itemTable.addCell(new Cell().setTextAlignment(TextAlignment.LEFT).add(new Paragraph(invoice.getInvoiceServiceType())));
            itemTable.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(invoice.getTotalUsed()))));
            itemTable.addCell(new Cell().setTextAlignment(TextAlignment.RIGHT).add(new Paragraph("N$" + format(invoice.getUnitPrice()))));
            itemTable.addCell(new Cell().setTextAlignment(TextAlignment.RIGHT).add(new Paragraph("N$" + format(invoice.getTotalAmount()))));

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
                    .add(new Paragraph("N$" + format(invoice.getTotalNetAmount())).setBold()));

            document.add(itemTable);

            // Payment Instructions
            document.add(new Paragraph("\nPayment Instructions").setBold().setMarginTop(20));
            document.add(new Paragraph("Bank: " + invoice.getBankName()));
            document.add(new Paragraph("Branch: " + invoice.getBranchName()));
            document.add(new Paragraph("Account Number: " + invoice.getAccountNumber()));

            // Terms & Conditions
            document.add(new Paragraph("\nTerms & Conditions").setBold().setMarginTop(20));
            document.add(new Paragraph(
                    "1. Payment is due within 15 days of the invoice date.\n" +
                            "2. Late payments may be subject to a finance charge.\n" +
                            "3. All services are provided as per the agreement between NAMSA / NASCAM and the client.\n" +
                            "4. This invoice is valid for payment upon approval of the associated music royalty usage."
            ).setFontSize(10).setFontColor(ColorConstants.GRAY));

            // Footer
            document.add(new Paragraph("\nThank you for your business!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setItalic()
                    .setMarginTop(30)
                    .setFontColor(ColorConstants.BLUE)
                    .setFontSize(12));

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

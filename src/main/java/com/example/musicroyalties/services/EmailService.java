package com.example.musicroyalties.services;

import com.example.musicroyalties.models.invoiceAndPayments.Invoice;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private PdfInvoiceGenerator pdfInvoiceGenerator;

    private static final String LOGO_URL = "https://ytnafrica.ggff.net/images/ytnlogo.png";
    
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    
//    public void sendProfileRejectionEmail(String email, String notes) {
//        String subject = "Profile Rejection Notification";
//        String body = "Dear Artist,\n\n" +
//                     "Your profile has been rejected by the administrator.\n\n" +
//                     "Reason: " + notes + "\n\n" +
//                     "Please review and resubmit your profile with the necessary corrections.\n\n" +
//                     "Best regards,\nMusic Royalties System";
//        sendEmail(email, subject, body);
//    }

    //profile Rejection
    public void sendProfileRejectionEmail(String email, String notes) {
        String subject = "Profile Rejection Notification";
        String logoUrl = "https://ytnafrica.ggff.net/images/ytnlogo.png"; // Same logo
        String resubmitLink = "http://localhost:8080/profile/resubmit"; // Update with your actual resubmission link

        String htmlBody =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #f0f0f0; border-radius: 10px; background-color: #fff8f8;'>" +
                        "<div style='text-align: center; padding-bottom: 20px; border-bottom: 2px solid #e86d6d;'>" +
                        "<img src='" + logoUrl + "' alt='YTN Africa Logo' style='height: 60px; object-fit: contain;' />" +
                        "</div>" +

                        "<div style='padding: 30px; color: #444; line-height: 1.6;'>" +
                        "<h2 style='color: #cc0000; margin-top: 0;'>Dear Artist,</h2>" +
                        "<p style='font-size: 16px; color: #333;'>We regret to inform you that your profile has been <strong>rejected</strong> by the administrator.</p>" +

                        "<p style='font-size: 16px; color: #cc0000;'><strong>Reason:</strong> " + notes + "</p>" +

                        "<p style='font-size: 16px;'>Please review the feedback above and resubmit your profile after making the necessary corrections.</p>" +

                        "<div style='text-align: center; margin: 30px 0;'>" +
                        "<a href='" + resubmitLink + "' " +
                        "style='background-color: #cc0000; color: white; padding: 14px 28px; text-decoration: none; font-size: 16px; border-radius: 6px; display: inline-block; font-weight: bold;'>" +
                        "Resubmit Profile" +
                        "</a>" +
                        "</div>" +

                        "<p style='font-size: 14px; color: #666;'>If the button doesn't work, copy and paste the following link into your browser:</p>" +
                        "<p style='font-size: 14px; word-break: break-all; color: #cc0000;'>" + resubmitLink + "</p>" +
                        "</div>" +

                        "<div style='text-align: center; padding-top: 20px; border-top: 1px solid #eaeaea; color: #999; font-size: 13px;'>" +
                        "<p>&copy; 2025 Music Royalties System. All rights reserved.</p>" +
                        "<p>For support, contact us at <a href='mailto:support@ytnafrica.com' style='color: #cc0000; text-decoration: none;'>support@ytnafrica.com</a></p>" +
                        "</div>" +
                        "</div>";

        try {
            jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // HTML email
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send profile rejection email", e);
        }
    }


    public void sendMusicRejectionEmail(String email, String notes) {
        String subject = "Music File Rejection Notification";
        String body = "Dear Artist,\n\n" +
                     "Your music file has been rejected by the administrator.\n\n" +
                     "Reason: " + notes + "\n\n" +
                     "Please review and resubmit your music file with the necessary corrections.\n\n" +
                     "Best regards,\nMusic Royalties System";
        sendEmail(email, subject, body);
    }
    
//    public void sendProfileApprovalEmail(String email) {
//        String subject = "Profile Approval Notification";
//        String body = "Dear Artist,\n\n" +
//                     "Congratulations! Your profile has been approved by the administrator.\n\n" +
//                     "You can now upload your music files.\n\n" +
//                     "Best regards,\nMusic Royalties System";
//        sendEmail(email, subject, body);
//    }

    //simple Profile Approval
    public void sendProfileApprovalEmail(String email) {
        String subject = "Profile Approval Notification";
        String logoUrl = "https://ytnafrica.ggff.net/images/ytnlogo.png"; // Reuse your logo
        String dashboardLink = "http://localhost:8080/dashboard"; // Link to your system (update if needed)

        String htmlBody =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #f0f0f0; border-radius: 10px; background-color: #f9f7ff;'>" +
                        "<div style='text-align: center; padding-bottom: 20px; border-bottom: 2px solid #9b6de8;'>" +
                        "<img src='" + logoUrl + "' alt='YTN Africa Logo' style='height: 60px; object-fit: contain;' />" +
                        "</div>" +

                        "<div style='padding: 30px; color: #444; line-height: 1.6;'>" +
                        "<h2 style='color: #6a0dad; margin-top: 0;'>Dear Artist,</h2>" +
                        "<p style='font-size: 16px; color: #333;'>🎉 Congratulations! Your profile has been <strong>approved</strong> by the administrator.</p>" +
                        "<p style='font-size: 16px;'>You can now upload and manage your music files in the system.</p>" +

                        "<div style='text-align: center; margin: 30px 0;'>" +
                        "<a href='" + dashboardLink + "' " +
                        "style='background-color: #8a2be2; color: white; padding: 14px 28px; text-decoration: none; font-size: 16px; border-radius: 6px; display: inline-block; font-weight: bold;'>" +
                        "Go to Dashboard" +
                        "</a>" +
                        "</div>" +

                        "<p style='font-size: 14px; color: #666;'>If the button doesn't work, copy and paste the following link into your browser:</p>" +
                        "<p style='font-size: 14px; word-break: break-all; color: #8a2be2;'>" + dashboardLink + "</p>" +
                        "</div>" +

                        "<div style='text-align: center; padding-top: 20px; border-top: 1px solid #eaeaea; color: #999; font-size: 13px;'>" +
                        "<p>&copy; 2025 Music Royalties System. All rights reserved.</p>" +
                        "<p>For support, contact us at <a href='mailto:support@ytnafrica.com' style='color: #8a2be2; text-decoration: none;'>support@ytnafrica.com</a></p>" +
                        "</div>" +
                        "</div>";

        try {
            jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // HTML email
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send profile approval email", e);
        }
    }


    public void sendMusicApprovalEmail(String email) {
        String subject = "Music File Approval Notification";
        String body = "Dear Artist,\n\n" +
                     "Congratulations! Your music file has been approved by the administrator.\n\n" +
                     "Your music is now available for companies to select.\n\n" +
                     "Best regards,\nMusic Royalties System";
        sendEmail(email, subject, body);
    }

    public void sendVerificationEmail(String email, String token) {
        String subject = "Email Verification";
        String logoUrl = "https://ytnafrica.ggff.net/images/ytnlogo.png"; // Make sure this URL is accessible
        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;

        String htmlBody =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #f0f0f0; border-radius: 10px; background-color: #f9f7ff;'>" +
                        "<div style='text-align: center; padding-bottom: 20px; border-bottom: 2px solid #9b6de8;'>" +
                        "<img src='" + logoUrl + "' alt='YTN Africa Logo' style='height: 60px; object-fit: contain;' />" +
                        "</div>" +

                        "<div style='padding: 30px; color: #444; line-height: 1.6;'>" +
                        "<h2 style='color: #6a0dad; margin-top: 0;'>Hello, ytnafrica member!</h2>" +
                        "<p style='font-size: 16px; color: #333;'>Thank you for registering with <strong>ytnafrica / nascam</strong>.</p>" +
                        "<p style='font-size: 16px;'>To complete your registration, please verify your email address by clicking the button below:</p>" +

                        "<div style='text-align: center; margin: 30px 0;'>" +
                        "<a href='" + verificationLink + "' " +
                        "style='background-color: #8a2be2; color: white; padding: 14px 28px; text-decoration: none; font-size: 16px; border-radius: 6px; display: inline-block; font-weight: bold;'>" +
                        "Verify Email Address" +
                        "</a>" +
                        "</div>" +

                        "<p style='font-size: 14px; color: #666;'>If the button doesn't work, copy and paste the following link into your browser:</p>" +
                        "<p style='font-size: 14px; word-break: break-all; color: #8a2be2;'>" + verificationLink + "</p>" +
                        "</div>" +

                        "<div style='text-align: center; padding-top: 20px; border-top: 1px solid #eaeaea; color: #999; font-size: 13px;'>" +
                        "<p>&copy; 2025 YTNAFRICA / NASCAM. All rights reserved.</p>" +
                        "<p>For support, contact us at <a href='mailto:support@ytnafrica.com' style='color: #8a2be2; text-decoration: none;'>support@ytnafrica.com</a></p>" +
                        "</div>" +
                        "</div>";

        try {
            jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = is HTML
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    // invoice pdf
    public void sendInvoiceEmail(String clientEmail, Invoice invoice) {
        String subject = "Invoice #" + invoice.getInvoiceNumber() + " - YTNAFRICA ";

        String htmlBody = buildInvoiceHtml(invoice);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // multipart

            helper.setTo(clientEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // HTML

            // Attach PDF
            byte[] pdfBytes = pdfInvoiceGenerator.generatePdf(invoice);
            helper.addAttachment("Invoice_" + invoice.getInvoiceNumber() + ".pdf",
                    new ByteArrayResource(pdfBytes));

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send invoice email", e);
        }
    }

    private String buildInvoiceHtml(Invoice invoice) {
        return """
            <div style='font-family: Arial, sans-serif; max-width: 700px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 12px; background-color: #ffffff; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>
                <div style='text-align: center; padding-bottom: 20px; border-bottom: 2px solid #8a2be2;'>
                    <img src='%s' alt='YTN Africa Logo' style='height: 70px; object-fit: contain;' />
                    <h1 style='color: #333; margin: 10px 0;'>INVOICE</h1>
                </div>

                <div style='padding: 25px; color: #333; line-height: 1.7; font-size: 15px;'>

                    <!-- Company & Client Info -->
                    <table width='100%%' style='margin-bottom: 25px;'>
                        <tr>
                            <td width='50%%'>
                                <strong>From:</strong><br/>
                                <strong>YTNAfrica / NASCAM</strong><br/>
                                %s<br/>
                                Phone: %s<br/>
                                Email: %s<br/>
                                Contact: %s
                            </td>
                            <td width='50%%' style='text-align: right;'>
                                <strong>Bill To:</strong><br/>
                                <strong>%s</strong><br/>
                                %s<br/>
                                Phone: %s<br/>
                                Email: %s
                            </td>
                        </tr>
                    </table>

                    <!-- Invoice Details -->
                    <table width='100%%' style='margin-bottom: 25px; border-collapse: collapse;'>
                        <tr>
                            <th style='text-align: left; padding: 8px; background-color: #f5f5f5;'>Invoice #</th>
                            <th style='text-align: left; padding: 8px; background-color: #f5f5f5;'>Date</th>
                            <th style='text-align: left; padding: 8px; background-color: #f5f5f5;'>Service</th>
                        </tr>
                        <tr>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'>%s</td>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'>%s</td>
                            <td style='padding: 8px; border-bottom: 1px solid #eee;'>%s</td>
                        </tr>
                    </table>

                    <!-- Amounts -->
                    <table width='100%%' style='margin-bottom: 25px; border-collapse: collapse;'>
                        <tr style='background-color: #f9f9f9;'>
                            <td style='padding: 10px;'><strong>Units Used:</strong> %d</td>
                            <td style='padding: 10px;'><strong>Unit Price:</strong> N$%s</td>
                        </tr>
                        <tr>
                            <td style='padding: 10px;'><strong>Total Amount:</strong> N$%s</td>
                            <td style='padding: 10px;'><strong>Net Amount:</strong> N$%s</td>
                        </tr>
                    </table>

                    <!-- Bank Info -->
                    <div style='background-color: #f0f8ff; padding: 15px; border-radius: 8px; margin-bottom: 20px;'>
                        <strong>Payment Instructions:</strong><br/>
                        Bank: %s<br/>
                        Branch: %s<br/>
                        Account Number: %d
                    </div>

                    <!-- Footer -->
                    <div style='text-align: center; padding: 15px; color: #777; font-size: 13px; border-top: 1px solid #eee; margin-top: 20px;'>
                        <p>&copy; 2025 ytnafrica. All rights reserved.</p>
                        <p>If you have questions, contact <a href='mailto:support@ytnafrica.com' style='color: #8a2be2;'>support@ytnafrica.com</a></p>
                    </div>
                </div>
            </div>
            """.formatted(
                LOGO_URL,
                invoice.getCompanyAddress(),
                invoice.getCompanyPhone(),
                invoice.getCompanyEmail(),
                invoice.getContactPerson(),
                invoice.getBillingToCompanyName(),
                invoice.getBillingToCompanyAddress(),
                invoice.getBillingToCompanyPhone(),
                invoice.getBillingToCompanyEmail(),
                invoice.getInvoiceNumber(),
                invoice.getInvoiceDate(),
                invoice.getInvoiceServiceType(),
                invoice.getTotalUsed(),
                format(invoice.getUnitPrice()),
                format(invoice.getTotalAmount()),
                format(invoice.getTotalNetAmount()),
                invoice.getBankName(),
                invoice.getBranchName(),
                invoice.getAccountNumber()
        );
    }

    private String format(Double value) {
        if (value == null) return "0.00";
        return String.format("%.2f", value);
    }

    //
}

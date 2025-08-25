package com.example.musicroyalties.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
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
}

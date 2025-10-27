//package com.EIPplatform.service.permitshistory;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@Slf4j
//public class NotificationServiceImpl implements NotificationService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Value("${app.notification.email.from}")
//    private String fromEmail;
//
//    @Value("${app.notification.email.from-name}")
//    private String fromName;
//
//    @Override
//    public void sendVerificationEmail(String email, String token) {
//        log.info("Sending verification email to: {}", email);
//
//        String subject = "Account Verification - Environmental Reporting System";
//        String verificationUrl = "http://localhost:8080/api/v1/auth/verify-email?token=" + token;
//        String body = "Hello,\n\n" +
//                "Please click the link below to verify your account:\n" +
//                verificationUrl + "\n\n" +
//                "This link will expire in 24 hours.\n\n" +
//                "Best regards,\n" +
//                "Environmental Reporting System";
//
//        sendEmail(email, subject, body);
//    }
//
//    @Override
//    public void sendPasswordResetEmail(String email, String token) {
//        log.info("Sending password reset email to: {}", email);
//
//        String subject = "Password Reset - Environmental Reporting System";
//        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
//        String body = "Hello,\n\n" +
//                "You have requested to reset your password. Please click the link below:\n" +
//                resetUrl + "\n\n" +
//                "This link will expire in 1 hour.\n\n" +
//                "If you did not request a password reset, please ignore this email.\n\n" +
//                "Best regards,\n" +
//                "Environmental Reporting System";
//
//        sendEmail(email, subject, body);
//    }
//
//    @Override
//    public void sendReportSubmittedNotification(UUID reportId) {
//        log.info("Sending report submitted notification for report: {}", reportId);
//        // TODO: Retrieve report details and send email
//    }
//
//    @Override
//    public void sendReportApprovedNotification(UUID reportId) {
//        log.info("Sending report approved notification for report: {}", reportId);
//        // TODO: Retrieve report details and send email
//    }
//
//    @Override
//    public void sendReportRejectedNotification(UUID reportId, String reason) {
//        log.info("Sending report rejected notification for report: {}", reportId);
//        // TODO: Retrieve report details and send email including rejection reason
//    }
//
//    @Override
//    public void sendReportReminderNotification(UUID userId, UUID reportId, int daysRemaining) {
//        log.info("Sending report reminder notification to user: {}, report: {}, days remaining: {}",
//                userId, reportId, daysRemaining);
//        // TODO: Send reminder email
//    }
//
//    @Override
//    public void sendReportOverdueNotification(UUID userId, UUID reportId) {
//        log.info("Sending overdue report notification to user: {}, report: {}", userId, reportId);
//        // TODO: Send overdue notification
//    }
//
//    @Override
//    public void sendPermitExpiryNotification(UUID userId, Long permitId, int daysRemaining) {
//        log.info("Sending permit expiry notification to user: {}, permit: {}, days remaining: {}",
//                userId, permitId, daysRemaining);
//        // TODO: Send expiry alert email
//    }
//
//    private void sendEmail(String to, String subject, String body) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(fromEmail);
//            message.setTo(to);
//            message.setSubject(subject);
//            message.setText(body);
//
//            mailSender.send(message);
//            log.info("Email sent successfully to: {}", to);
//        } catch (Exception e) {
//            log.error("Failed to send email to: {}", to, e);
//        }
//    }
//}

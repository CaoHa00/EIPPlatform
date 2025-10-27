//package com.EIPplatform.service.permitshistory;
//
//import java.util.UUID;
//
//public interface NotificationService {
//
//    /**
//     * Gửi email xác thực
//     */
//    void sendVerificationEmail(String email, String token);
//
//    /**
//     * Gửi email reset password
//     */
//    void sendPasswordResetEmail(String email, String token);
//
//    /**
//     * Gửi thông báo báo cáo đã submit
//     */
//    void sendReportSubmittedNotification(UUID reportId);
//
//    /**
//     * Gửi thông báo báo cáo đã được duyệt
//     */
//    void sendReportApprovedNotification(UUID reportId);
//
//    /**
//     * Gửi thông báo báo cáo bị từ chối
//     */
//    void sendReportRejectedNotification(UUID reportId, String reason);
//
//    /**
//     * Gửi nhắc nhở submit báo cáo
//     */
//    void sendReportReminderNotification(UUID userId, UUID reportId, int daysRemaining);
//
//    /**
//     * Gửi cảnh báo báo cáo quá hạn
//     */
//    void sendReportOverdueNotification(UUID userId, UUID reportId);
//
//    /**
//     * Gửi thông báo giấy phép sắp hết hạn
//     */
//    void sendPermitExpiryNotification(UUID userId, Long permitId, int daysRemaining);
//}
package com.EIPplatform.controller.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    JavaMailSender mailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    String fromEmail;

    @Async("emailExecutor")
    public void sendPasswordChangeVerificationCode(String toEmail, String code, String fullName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Password Change Verification Code");

            String htmlContent = buildPasswordChangeEmailHtml(code, fullName);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildPasswordChangeEmailHtml(String code, String fullName) {
        return """
                                 <!DOCTYPE html>
                <html lang="en">
                  <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>Password Change Verification</title>
                    <style>
                      body {
                        font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        margin: 0;
                        padding: 0;
                      }

                      .email-wrapper {
                        padding: 40px 20px;
                        min-height: 100vh;
                      }

                      .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background: rgb(255, 255, 255);
                        border-radius: 16px;
                        overflow: hidden;
                        position: relative;
                        border: 1px solid;
                        border-color: rgb(223, 223, 223);
                      }

                      .header {
                        text-align: center;
                        padding: 40px 40px 20px;
                        background: linear-gradient(
                          135deg,
                          rgb(0, 119, 255),
                          rgba(0, 248, 227, 0.651)
                        );
                      }

                      .logo {
                        width: 240px;
                        height: auto;
                        margin: 0 auto 20px;
                        padding: 10px;
                        background: white;
                        border-radius: 12px;
                        box-shadow: 0 4px 12px rgba(5, 0, 38, 0.1);
                        border: 2px solid rgba(32, 91, 204, 0.1);
                      }

                      .logo img {
                        width: 100%%;
                        height: auto;
                        object-fit: contain;
                        display: block;
                        max-width: 200px;
                        margin: 0 auto;
                      }

                      .header h1 {
                        color: #f3f3f3;
                        font-size: 28px;
                        font-weight: 600;
                        margin: 0 0 8px 0;
                      }

                      .header p {
                        color: #f3fbff;
                        font-size: 16px;
                        margin: 0;
                      }

                      .content {
                        padding: 10px 40px;
                      }

                      .greeting {
                        font-size: 18px;
                        color: #050026;
                        font-weight: 500;
                        margin-bottom: 20px;
                      }

                      .message {
                        font-size: 16px;
                        color: #444;
                        margin-bottom: 30px;
                        line-height: 1.6;
                      }

                      .code-section {
                        text-align: center;
                        margin: 30px 0;
                      }

                      .code-label {
                        font-size: 14px;
                        color: #666;
                        margin-bottom: 12px;
                        text-transform: uppercase;
                        letter-spacing: 0.5px;
                        font-weight: bold;
                      }

                      .code-box {
                        background: linear-gradient(
                          135deg,
                          rgba(15, 77, 168, 0.05),
                          rgba(32, 91, 204, 0.05)
                        );
                        border: 2px dashed #ebebeb;
                        padding: 25px;
                        border-radius: 12px;
                        margin: 15px 0;
                        position: relative;
                        overflow: hidden;
                      }

                      .code-box::before {
                        content: "";
                        position: absolute;
                        top: 0;
                        left: 0;
                        right: 0;
                        bottom: 0;
                        background: linear-gradient(
                          45deg,
                          transparent 30%%,
                          rgba(50, 224, 255, 0.137) 50%%,
                          transparent 70%%
                        );
                        animation: shimmer 2s infinite;
                      }

                      @keyframes shimmer {
                        0%% {
                          transform: translateX(-100%%);
                        }
                        100%% {
                          transform: translateX(100%%);
                        }
                      }

                      .code {
                        font-size: 32px;
                        font-weight: bold;
                        color: hsl(194, 100%%, 50%%);
                        letter-spacing: 8px;
                        font-family: "Courier New", monospace;
                        position: relative;
                        z-index: 1;
                        text-shadow: 0 2px 4px rgba(15, 77, 168, 0.1);
                      }

                      .expiry-notice {
                        font-size: 13px;
                        color: #ff6b35;
                        margin-top: 12px;
                        font-weight: 500;
                      }

                      .support-section {
                        background: rgba(104, 179, 245, 0.74);
                        border-radius: 8px;
                        padding: 20px;
                        margin: 25px 0;
                        text-align: center;
                      }

                      .support-text {
                        color: #00aeff;
                        font-size: 15px;
                        margin-bottom: 15px;
                      }

                      .support-button {
                        display: inline-block;
                        background: linear-gradient(135deg, #0f4da8, #205bcc);
                        color: white;
                        padding: 12px 24px;
                        text-decoration: none;
                        border-radius: 8px;
                        font-weight: 500;
                        transition: all 0.3s ease;
                      }

                      .footer {
                        background: linear-gradient(
                          135deg,
                          rgb(66, 255, 246),
                          rgba(0, 28, 187, 0.651)
                        );
                        text-align: center;
                        padding: 30px 40px;
                        border-top: 1px solid #eee;
                      }

                      .footer p {
                        font-size: 13px;
                        color: #ffffff;
                        margin: 5px 0;
                      }

                      .company-name {
                        color: #45fff6;
                        font-weight: 600;
                      }

                      /* Mobile responsiveness */
                      @media (max-width: 600px) {
                        .email-wrapper {
                          padding: 20px 10px;
                        }

                        .header,
                        .content,
                        .footer {
                          padding-left: 20px;
                          padding-right: 20px;
                        }

                        .code {
                          font-size: 24px;
                          letter-spacing: 4px;
                        }

                        .header h1 {
                          font-size: 24px;
                        }
                      }
                    </style>
                  </head>
                  <body>
                    <div class="email-wrapper">
                      <div class="container">
                        <div class="header">
                          <div class="logo">
                            <img
                              src="https://77qw0m4som.ufs.sh/f/ctpeNEM0o6WOKZKjxnmRTPS7miVkghBXFyduAzlWLHI6c2qN"
                              alt="Center Logo"
                            />
                          </div>
                          <h1>Password Change Verification</h1>
                          <p>Secure verification required</p>
                        </div>

                        <div class="content">
                          <p class="greeting">Hello %s,</p>

                          <p class="message">
                            We received a request to change your password. To ensure the
                            security of your account, please use the verification code below to
                            complete this process.
                          </p>

                          <div class="code-section">
                            <div class="code-label">Your Verification Code</div>
                            <div class="code-box">
                              <div class="code">%s</div>
                            </div>
                            <div class="expiry-notice">⏰ This code expires in 10 minutes</div>
                          </div>
                        </div>

                        <div class="footer">
                          <p>
                            This is an automated security message. Please do not reply to this
                            email.
                          </p>
                          <p>
                            &copy; 2025 <span class="company-name">IIC 4.0</span>. All rights
                            reserved.
                          </p>
                          <p>Powered by secure verification technology</p>
                        </div>
                      </div>
                    </div>
                  </body>
                </html>

                                                                """
                .formatted(fullName != null ? fullName : "User", code);
    }
}
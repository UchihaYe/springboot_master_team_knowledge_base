package com.teamkb.service;

import com.teamkb.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${spring.mail.username:noreply@teamkb.com}")
    private String fromEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(fromEmail);
        message.setSubject("Verify your email address");

        String verificationUrl = baseUrl + "/api/v1/auth/verify-email?token=" + user.getEmailVerificationToken();
        message.setText("Please click the following link to verify your email address:\n\n" + verificationUrl);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            // Log error but don't fail the registration process
            System.err.println("Failed to send verification email: " + e.getMessage());
        }
    }

    public void sendPasswordResetEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(fromEmail);
        message.setSubject("Reset your password");

        String resetUrl = baseUrl + "/auth/reset-password?token=" + user.getPasswordResetToken();
        message.setText("Please click the following link to reset your password:\n\n" + resetUrl +
                       "\n\nThis link will expire in 1 hour.");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
        }
    }

    public void sendNotificationEmail(User user, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(fromEmail);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send notification email: " + e.getMessage());
        }
    }
}
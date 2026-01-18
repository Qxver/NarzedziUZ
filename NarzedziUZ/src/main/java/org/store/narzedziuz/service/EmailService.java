package org.store.narzedziuz.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmailWithAttachment(String to, String subject, String body, byte[] pdfBytes, String filename) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // true oznacza multipart (czyli obsługę załączników)
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("twoj.email@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            // Dodawanie załącznika PDF
            helper.addAttachment(filename, new ByteArrayResource(pdfBytes));

            mailSender.send(message);
            System.out.println("E-mail z fakturą wysłany do: " + to);

        } catch (MessagingException e) {
            System.err.println("Błąd wysyłania maila: " + e.getMessage());
        }
    }
}

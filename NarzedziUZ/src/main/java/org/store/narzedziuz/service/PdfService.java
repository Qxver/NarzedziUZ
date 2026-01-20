package org.store.narzedziuz.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont; // WAŻNE IMPORTY
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.store.narzedziuz.entity.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;

    public byte[] generateInvoicePdf(Order order) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            Context context = new Context();
            context.setVariable("order", order);
            context.setVariable("paymentDueDate", LocalDate.now().plusDays(14));

            String htmlContent = templateEngine.process("invoice_template", context);

            ITextRenderer renderer = new ITextRenderer();

            String fontPath = new ClassPathResource("fonts/OpenSans-Regular.ttf").getURL().toExternalForm();

            renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);

            return os.toByteArray();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Błąd generowania PDF: " + e.getMessage(), e);
        }
    }
}

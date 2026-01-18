package org.store.narzedziuz.service;

import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.store.narzedziuz.entity.Order;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;

    // Metoda generująca bajty PDF-a dla danego zamówienia
    public byte[] generateInvoicePdf(Order order) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            // 1. Przygotuj kontekst (zmienne dla Thymeleaf)
            Context context = new Context();
            context.setVariable("order", order);

            // --- POPRAWKA: Dodajemy brakujące daty ---
            context.setVariable("paymentDueDate", LocalDate.now().plusDays(14)); // Termin płatności
            // ----------------------------------------

            // 2. Przetwórz HTML
            String htmlContent = templateEngine.process("invoice_template", context);

            // 3. Wygeneruj PDF
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);

            return os.toByteArray();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Błąd generowania PDF: " + e.getMessage(), e);
        }
    }
}

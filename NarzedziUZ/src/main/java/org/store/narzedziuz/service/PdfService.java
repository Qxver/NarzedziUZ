package org.store.narzedziuz.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Dodaj import
import org.store.narzedziuz.entity.Order;
import org.store.narzedziuz.repository.OrderRepository; // Dodaj import
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
    private final OrderRepository orderRepository; // <--- 1. WSTRZYKNIJ REPOZYTORIUM

    // Warto dodać Transactional, żeby sesja była aktywna podczas przetwarzania szablonu
    @Transactional(readOnly = true)
    public byte[] generateInvoicePdf(Order simpleOrder) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            // <--- 2. ODŚWIEŻ ZAMÓWIENIE --->
            // Nawet jeśli przekazujesz obiekt 'simpleOrder', tutaj pobieramy go jeszcze raz
            // z bazy RAZEM z produktami, żeby mieć pewność, że są załadowane dla maila.
            Order fullOrder = orderRepository.findByIdWithItems(simpleOrder.getOrderId())
                    .orElse(simpleOrder); // Fallback do przekazanego, jeśli nie znajdzie (mało prawdopodobne)

            Context context = new Context();
            context.setVariable("order", fullOrder); // <--- 3. UŻYJ PEŁNEGO OBIEKTU
            context.setVariable("paymentDueDate", LocalDate.now().plusDays(14));

            // Reszta bez zmian...
            String htmlContent = templateEngine.process("invoice_template", context);

            ITextRenderer renderer = new ITextRenderer();

            // Upewnij się, że ścieżka do fontu jest poprawna w kontenerze/Dockerze
            // Czasami ClassPathResource wewnątrz JARa wymaga podejścia przez InputStream,
            // ale jeśli działa Ci przy pobieraniu, to tu też zadziała.
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

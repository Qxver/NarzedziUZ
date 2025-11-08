package org.store.narzedziuz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_history")
@Data
public class EmailHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long emailId;

    @Column(name = "recipient_email")
    private String recipientEmail;

    private String subject;

    @Lob
    private String body;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    private String status;

    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
    }
}

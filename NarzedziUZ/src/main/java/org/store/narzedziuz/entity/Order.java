package org.store.narzedziuz.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "price_sum")
    private BigDecimal priceSum;

    @Column(name = "deliver_address")
    private String deliverAddress;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "payment_method")
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ToString.Exclude // <--- DODAJ TO (wymaga importu lombok.ToString)
    private User user;

    @OneToMany(mappedBy = "order")
    @ToString.Exclude // <--- I TO TEŻ
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order")
    private Invoice invoice;


    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
        orderStatus = "PENDING";
    }
    public String getFormattedDate() {
        if (this.orderDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.orderDate.format(formatter);
    }
}


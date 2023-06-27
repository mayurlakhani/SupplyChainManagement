package com.microservices.orderservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "OrderItem")
public class OrderLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderItem_seq")
    @SequenceGenerator(name = "orderItem_seq", sequenceName = "orderItem_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
package com.example.http_trading_platform.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "portfolio_item")
public class PortfolioItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String ticker;
    private int quantity;

    public PortfolioItem(int id, Customer customer, String ticker, int quantity) {
        this.id = id;
        this.customer = customer;
        this.ticker = ticker;
        this.quantity = quantity;
    }
}

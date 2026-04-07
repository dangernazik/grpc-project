package com.example.http_trading_platform.user.model;

import com.example.http_trading_platform.common.Ticker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Holding {
    private Ticker ticker;
    private int quantity;

    public Holding(Ticker ticker, int quantity) {
        this.ticker = ticker;
        this.quantity = quantity;
    }
}

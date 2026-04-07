package com.example.http_trading_platform.user.model;


import com.example.http_trading_platform.common.Ticker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockTradeResponse {

    private int userId;
    private Ticker ticker;
    private int price;
    private int quantity;
    private TradeAction action;
    private int totalPrice;
    private int balance;
}

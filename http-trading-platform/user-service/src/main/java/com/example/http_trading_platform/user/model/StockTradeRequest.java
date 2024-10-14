package com.example.http_trading_platform.user.model;

import com.example.http_trading_platform.common.Ticker;
import lombok.Data;

@Data
public class StockTradeRequest {
    private int userId;
    private Ticker ticker;
    private int price;
    private int quantity;
    private TradeAction action;
}

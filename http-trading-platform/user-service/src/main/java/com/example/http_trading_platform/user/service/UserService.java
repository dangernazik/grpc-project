package com.example.http_trading_platform.user.service;

import com.example.http_trading_platform.user.model.StockTradeRequest;
import com.example.http_trading_platform.user.model.StockTradeResponse;
import com.example.http_trading_platform.user.model.UserInformation;

public interface UserService {

    StockTradeResponse buyStock(StockTradeRequest request);

    StockTradeResponse sellStock(StockTradeRequest request);

    UserInformation getUserInformation(int userId);

    UserInformation createUser(UserInformation userInfo);
}
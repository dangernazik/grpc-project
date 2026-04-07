package com.example.http_trading_platform.user.controller;

import com.example.http_trading_platform.user.model.StockTradeRequest;
import com.example.http_trading_platform.user.model.StockTradeResponse;
import com.example.http_trading_platform.user.model.UserInformation;
import com.example.http_trading_platform.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/buy")
    public StockTradeResponse buyStock(@RequestBody StockTradeRequest request) {
        return userService.buyStock(request);
    }

    @PostMapping("/sell")
    public StockTradeResponse sellStock(@RequestBody StockTradeRequest request) {
        return userService.sellStock(request);
    }

    @GetMapping("/{userId}")
    public UserInformation getUserInformation(@PathVariable int userId) {
        return userService.getUserInformation(userId);
    }

    @PostMapping("/create")
    public UserInformation createUser(@RequestBody UserInformation userInfo) {
        return userService.createUser(userInfo);
    }
}

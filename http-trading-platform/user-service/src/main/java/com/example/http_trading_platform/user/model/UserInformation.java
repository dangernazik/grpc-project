package com.example.http_trading_platform.user.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class UserInformation {
    private int userId;
    private String name;
    private int balance;
    private List<Holding> holdings;

    public UserInformation(int userId, String name, int balance, List<Holding> holdings) {
        this.userId = userId;
        this.name = name;
        this.balance = balance;
        this.holdings = holdings;
    }
}

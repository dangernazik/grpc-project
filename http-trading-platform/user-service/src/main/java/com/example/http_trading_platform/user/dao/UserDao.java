package com.example.http_trading_platform.user.dao;


import com.example.http_trading_platform.common.Ticker;
import com.example.http_trading_platform.user.entity.Customer;
import com.example.http_trading_platform.user.entity.PortfolioItem;

import java.util.List;

public interface UserDao {

    Customer findCustomerById(int userId);

    PortfolioItem findPortfolioItemByCustomerAndTicker(Customer customer, Ticker ticker);

    void savePortfolioItem(PortfolioItem portfolioItem);

    void updateCustomer(Customer customer);

    void deletePortfolioItem(PortfolioItem portfolioItem);

    List<PortfolioItem> findPortfolioItemsByCustomer(Customer customer);

    void saveCustomer(Customer newCustomer);
}

package com.example.http_trading_platform.user.service;

import com.example.http_trading_platform.common.Ticker;
import com.example.http_trading_platform.user.dao.UserDao;
import com.example.http_trading_platform.user.entity.Customer;
import com.example.http_trading_platform.user.entity.PortfolioItem;
import com.example.http_trading_platform.user.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public StockTradeResponse buyStock(StockTradeRequest request) {
        Customer customer = userDao.findCustomerById(request.getUserId());
        int totalPrice = request.getPrice() * request.getQuantity();

        // Перевірка балансу
        if (customer.getBalance() < totalPrice) {
            throw new RuntimeException("Недостатньо коштів для купівлі");
        }

        // Оновлення балансу користувача
        customer.setBalance(customer.getBalance() - totalPrice);

        // Оновлення портфелю
        PortfolioItem portfolioItem = userDao.findPortfolioItemByCustomerAndTicker(customer, request.getTicker());
        if (portfolioItem == null) {
            portfolioItem = new PortfolioItem(0, customer, request.getTicker().name(), request.getQuantity());
        } else {
            portfolioItem.setQuantity(portfolioItem.getQuantity() + request.getQuantity());
        }
        userDao.savePortfolioItem(portfolioItem);
        userDao.updateCustomer(customer);

        // Формування відповіді
        return new StockTradeResponse(
                customer.getId(), request.getTicker(), request.getPrice(), request.getQuantity(),
                TradeAction.BUY, totalPrice, customer.getBalance()
        );
    }

    @Override
    public StockTradeResponse sellStock(StockTradeRequest request) {
        Customer customer = userDao.findCustomerById(request.getUserId());
        PortfolioItem portfolioItem = userDao.findPortfolioItemByCustomerAndTicker(customer, request.getTicker());

        // Перевірка наявності акцій у портфелі
        if (portfolioItem == null || portfolioItem.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Недостатньо акцій для продажу");
        }

        int totalPrice = request.getPrice() * request.getQuantity();

        // Оновлення балансу користувача
        customer.setBalance(customer.getBalance() + totalPrice);

        // Оновлення кількості акцій у портфелі
        portfolioItem.setQuantity(portfolioItem.getQuantity() - request.getQuantity());
        if (portfolioItem.getQuantity() == 0) {
            userDao.deletePortfolioItem(portfolioItem);
        } else {
            userDao.savePortfolioItem(portfolioItem);
        }
        userDao.updateCustomer(customer);

        // Формування відповіді
        return new StockTradeResponse(
                customer.getId(), request.getTicker(), request.getPrice(), request.getQuantity(),
                TradeAction.SELL, totalPrice, customer.getBalance()
        );
    }


    @Override
    public UserInformation getUserInformation(int userId) {
        Customer customer = userDao.findCustomerById(userId);
        List<PortfolioItem> portfolioItems = userDao.findPortfolioItemsByCustomer(customer);

        // Створюємо список утримань акцій
        List<Holding> holdings = portfolioItems.stream()
                .map(item -> new Holding(Ticker.valueOf(item.getTicker()), item.getQuantity()))
                .collect(Collectors.toList());

        // Повертаємо інформацію про користувача
        return new UserInformation(customer.getId(), customer.getName(), customer.getBalance(), holdings);
    }

    @Override
    @Transactional
    public UserInformation createUser(UserInformation userInfo) {
        System.out.println("Received user info: " + userInfo);

        Customer newCustomer = new Customer();
        newCustomer.setName(userInfo.getName());
        newCustomer.setBalance(userInfo.getBalance());

        // Перевірте значення перед збереженням
        System.out.println("Customer Name: " + newCustomer.getName());
        System.out.println("Customer Balance: " + newCustomer.getBalance());

        userDao.saveCustomer(newCustomer);

        return new UserInformation(newCustomer.getId(), newCustomer.getName(), newCustomer.getBalance(), new ArrayList<>());
    }
}

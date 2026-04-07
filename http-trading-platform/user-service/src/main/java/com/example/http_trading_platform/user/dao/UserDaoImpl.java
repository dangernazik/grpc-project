package com.example.http_trading_platform.user.dao;

import com.example.http_trading_platform.common.Ticker;
import com.example.http_trading_platform.user.entity.Customer;
import com.example.http_trading_platform.user.entity.PortfolioItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Customer findCustomerById(int id) {
        return entityManager.find(Customer.class, id);
    }

    @Override
    public PortfolioItem findPortfolioItemByCustomerAndTicker(Customer customer, Ticker ticker) {
        try {
            return entityManager.createQuery("SELECT p FROM PortfolioItem p WHERE p.customer = :customer AND p.ticker = :ticker", PortfolioItem.class)
                    .setParameter("customer", customer)
                    .setParameter("ticker", ticker.name())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void savePortfolioItem(PortfolioItem portfolioItem) {
        if (portfolioItem.getId() == 0) {
            entityManager.persist(portfolioItem);
        } else {
            entityManager.merge(portfolioItem);
        }
    }

    @Override
    public void deletePortfolioItem(PortfolioItem portfolioItem) {
        entityManager.remove(entityManager.contains(portfolioItem) ? portfolioItem : entityManager.merge(portfolioItem));
    }

    @Override
    public void updateCustomer(Customer customer) {
        entityManager.merge(customer);
    }

    @Override
    public List<PortfolioItem> findPortfolioItemsByCustomer(Customer customer) {
        return entityManager.createQuery("SELECT p FROM PortfolioItem p WHERE p.customer = :customer", PortfolioItem.class)
                .setParameter("customer", customer)
                .getResultList();
    }

    @Override
    public void saveCustomer(Customer customer) {
        entityManager.persist(customer);  // Зберігаємо нового користувача
    }
}
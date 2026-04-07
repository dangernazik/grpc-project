package com.vinsguru.user.service.handler;

import com.vinsguru.common.Ticker;
import com.vinsguru.user.StockTradeRequest;
import com.vinsguru.user.StockTradeResponse;
import com.vinsguru.user.exceptions.InsufficientBalanceException;
import com.vinsguru.user.exceptions.InsufficientSharesException;
import com.vinsguru.user.exceptions.UnknownTickerException;
import com.vinsguru.user.exceptions.UnknownUserException;
import com.vinsguru.user.repository.PortfolioItemRepository;
import com.vinsguru.user.repository.UserRepository;
import com.vinsguru.user.util.EntityMessageMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StockTradeRequestHandler {

    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public StockTradeRequestHandler(UserRepository userRepository, PortfolioItemRepository portfolioItemRepository) {
        this.userRepository = userRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    @Transactional
    public StockTradeResponse buyStock(StockTradeRequest request) {
        long start = System.currentTimeMillis();
        System.out.println("\n========== BUY STOCK ==========");

        this.validateTicker(request.getTicker());
        var user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UnknownUserException(request.getUserId()));
        var totalPrice = request.getQuantity() * request.getPrice();
        this.validateUserBalance(user.getId(), user.getBalance(), totalPrice);

        user.setBalance(user.getBalance() - totalPrice);
        this.portfolioItemRepository.findByUserIdAndTicker(user.getId(), request.getTicker())
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + request.getQuantity()),
                        () -> this.portfolioItemRepository.save(EntityMessageMapper.toPortfolioItem(request))
                );

        System.out.println("⏳ Flushing changes to database...");
        long flushStart = System.currentTimeMillis();
        this.entityManager.flush();
        long flushTime = System.currentTimeMillis() - flushStart;
        System.out.println("✅ Flush complete (DB write time): " + flushTime + " ms");

        long totalTime = System.currentTimeMillis() - start;
        System.out.println("🟢 Total buyStock execution time: " + totalTime + " ms");
        System.out.println("========== END BUY STOCK ==========\n");

        return EntityMessageMapper.toStockTradeResponse(request, user.getBalance());
    }


    @Transactional
    public StockTradeResponse sellStock(StockTradeRequest request) {
        long start = System.currentTimeMillis();
        System.out.println("\n========== SELL STOCK ==========");

        this.validateTicker(request.getTicker());
        var user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UnknownUserException(request.getUserId()));
        var portfolioItem = this.portfolioItemRepository.findByUserIdAndTicker(user.getId(), request.getTicker())
                .filter(pi -> pi.getQuantity() >= request.getQuantity())
                .orElseThrow(() -> new InsufficientSharesException(user.getId()));

        var totalPrice = request.getQuantity() * request.getPrice();
        user.setBalance(user.getBalance() + totalPrice);
        portfolioItem.setQuantity(portfolioItem.getQuantity() - request.getQuantity());

        System.out.println("⏳ Flushing changes to database...");
        long flushStart = System.currentTimeMillis();
        this.entityManager.flush();
        long flushTime = System.currentTimeMillis() - flushStart;
        System.out.println("✅ Flush complete (DB write time): " + flushTime + " ms");

        long totalTime = System.currentTimeMillis() - start;
        System.out.println("🟢 Total sellStock execution time: " + totalTime + " ms");
        System.out.println("========== END SELL STOCK ==========\n");

        return EntityMessageMapper.toStockTradeResponse(request, user.getBalance());
    }


    private void validateTicker(Ticker ticker) {
        if (Ticker.UNKNOWN.equals(ticker)) {
            throw new UnknownTickerException();
        }
    }

    private void validateUserBalance(Integer userId, Integer userBalance, Integer totalPrice) {
        if (totalPrice > userBalance) {
            throw new InsufficientBalanceException(userId);
        }
    }

}

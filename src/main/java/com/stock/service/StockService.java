package com.stock.service;


import com.stock.dto.OrderRequest;
import com.stock.dto.StockDTO;
import com.stock.entity.*;
import com.stock.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockService {

    private final UserRepository userRepository;
    private final StockDetailRepository stockRepo;
    private final WalletRepository walletRepository;
    private final FundRepository fundRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public StockService(
            UserRepository userRepository,
            StockDetailRepository stockRepo,
            WalletRepository walletRepository,
            FundRepository fundRepository,
            TransactionHistoryRepository transactionHistoryRepository
    ) {
        this.userRepository = userRepository;
        this.stockRepo = stockRepo;
        this.walletRepository = walletRepository;
        this.fundRepository = fundRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public List<StockDetail> getAllStocks() {
        return stockRepo.findAll();
    }

    @Transactional
    public void createOrder(OrderRequest req, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getAmount() < req.getAmount()) {
           double totalRemainingAmount= wallet.getAmount()- req.getAmount();
           wallet.setAmount(totalRemainingAmount);
            throw new RuntimeException("Insufficient funds in wallet");
        }

        wallet.setAmount(wallet.getAmount() - req.getAmount());
        walletRepository.save(wallet);

        StockDetail sd = new StockDetail();
        sd.setStockName(req.getNameOfStock());
        sd.setStockPrice(req.getAmount());
        sd.setStatusOfTheStock("PENDING");
        sd.setStockBuyDate(LocalDateTime.now());
        sd.setUser(user);

        if (req.getStatus().equalsIgnoreCase("buying")) {
            sd.setNumberOfKittaBought(req.getNumberOfKitta());
        } else {
            sd.setNumberOfKittaToBeSell(req.getNumberOfKitta());
        }

        stockRepo.save(sd);

        Fund fund = new Fund();
        fund.setUser(user);
        fund.setStockDetail(sd);
        fund.setLoadAmountInTheStock(req.getAmount());
        fund.setCreatedAt(LocalDateTime.now());
        fundRepository.save(fund);

        // Store transaction history
        TransactionHistory history = new TransactionHistory();
        history.setAmount(req.getAmount());
        history.setTimestamp(LocalDateTime.now());
        history.setType(req.getStatus().equals("buying") ? "BUY" : "SELL");
        history.setUser(user);
        history.setNumberOfKitta(req.getNumberOfKitta());
        history.setDetails("Order placed");
        transactionHistoryRepository.save(history);
    }


    // Approve pending orders automatically (scheduler)
    public void checkPendingOrdersAndUpdateStatus() {

        List<StockDetail> pending = stockRepo.findByStatusOfTheStock("PENDING");

        for (StockDetail sd : pending) {

            User user = sd.getUser();

            // For simplicity — assume every order is approved automatically
            sd.setStatusOfTheStock("APPROVED");
            stockRepo.save(sd);

            // Update user's total kitta
            if (sd.getNumberOfKittaBought() != null) {
                user.setNumberOfKitta(user.getNumberOfKitta() + sd.getNumberOfKittaBought());
            }

            if (sd.getNumberOfKittaToBeSell() != null) {
                user.setNumberOfKitta(user.getNumberOfKitta() - sd.getNumberOfKittaToBeSell());
            }

            userRepository.save(user);
        }
    }

    public String getStatus(Long orderId) {
        StockDetail sd = stockRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return sd.getStatusOfTheStock();
    }

    public void storeStockList(List<StockDTO> stocks) {
        for (StockDTO dto : stocks) {
            StockDetail sd = new StockDetail();
            sd.setStockName(dto.getStockName());
            sd.setStockPrice(dto.getPrice());
            sd.setStatusOfTheStock("LISTED");
            stockRepo.save(sd);
        }
    }
}


package com.stock.service;



import com.stock.entity.TransactionHistory;
import com.stock.entity.User;
import com.stock.entity.Wallet;
import com.stock.repository.TransactionHistoryRepository;
import com.stock.repository.UserRepository;
import com.stock.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionHistoryRepository historyRepository;
    private final RestTemplate restTemplate;

    public WalletService(
            UserRepository userRepository,
            WalletRepository walletRepository,
            TransactionHistoryRepository historyRepository,
            RestTemplate restTemplate
    ) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.historyRepository = historyRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public void loadAmount(String username, Double amount) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElse(new Wallet());

        // Third party payment API call
        Map<String, Object> request = new HashMap<>();
        request.put("username", username);
        request.put("amount", amount);

        // Example – mock API endpoint
        String paymentApi = "https://dummy-payment-provider.test/pay";

        try {
            restTemplate.postForEntity(paymentApi, request, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Payment failed: " + e.getMessage());
        }

        // Update wallet balance
        wallet.setUser(user);
        wallet.setUpdatedAt(LocalDateTime.now());
        wallet.setWalletName("STOCK WALLET");
        wallet.setAmount(wallet.getAmount() == null ? amount : wallet.getAmount() + amount);

        walletRepository.save(wallet);

        // Add transaction history
        TransactionHistory history = new TransactionHistory();
        history.setUser(user);
        history.setType("LOAD");
        history.setAmount(amount);
        history.setTimestamp(LocalDateTime.now());
        history.setDetails("Wallet recharge successful");
        historyRepository.save(history);
    }

    public void updateAmount(String username, Double amount) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setAmount(amount);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);
    }
}

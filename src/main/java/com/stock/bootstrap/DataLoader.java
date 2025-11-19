package com.stock.bootstrap;

import com.stock.entity.StockDetail;
import com.stock.entity.User;
import com.stock.entity.Wallet;
import com.stock.repository.StockDetailRepository;
import com.stock.repository.UserRepository;
import com.stock.repository.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private final StockDetailRepository stockRepo;
    private final UserRepository userRepo;
    private final WalletRepository walletRepo;

    public DataLoader(StockDetailRepository stockRepo, UserRepository userRepo, WalletRepository walletRepo) {
        this.stockRepo = stockRepo;
        this.userRepo = userRepo;
        this.walletRepo = walletRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        // -------- Users --------
        if (userRepo.count() == 0) {
            User user1 = new User();
            user1.setUsername("rimesh123");
            user1.setFirstName("Rimesh");
            user1.setLastName("Sapkota");
            user1.setNumberOfKitta(0);
            user1.setCreatedDate(LocalDateTime.now());

            User user2 = new User();
            user2.setUsername("johnDoe");
            user2.setFirstName("John");
            user2.setLastName("Doe");
            user2.setNumberOfKitta(0);
            user2.setCreatedDate(LocalDateTime.now());

            userRepo.saveAll(Arrays.asList(user1, user2));

            System.out.println("Bootstrap users loaded successfully!");
        }

        // -------- Wallets --------
        if (walletRepo.count() == 0) {
            User user1 = userRepo.findByUsername("rimesh123").orElseThrow();
            User user2 = userRepo.findByUsername("johnDoe").orElseThrow();

            Wallet wallet1 = new Wallet();
            wallet1.setWalletName("Rimesh Wallet");
            wallet1.setAmount(100000.0); // initial amount
            wallet1.setUser(user1);

            Wallet wallet2 = new Wallet();
            wallet2.setWalletName("John Wallet");
            wallet2.setAmount(50000.0);
            wallet2.setUser(user2);

            walletRepo.saveAll(Arrays.asList(wallet1, wallet2));

            System.out.println("Bootstrap wallets loaded successfully!");
        }

        // -------- Stock --------
        if (stockRepo.count() == 0) {
            User user1 = userRepo.findByUsername("rimesh123").orElseThrow();
            User user2 = userRepo.findByUsername("johnDoe").orElseThrow();

            StockDetail stock1 = new StockDetail();
            stock1.setStockName("Apple Inc.");
            stock1.setStockPrice(150.0);
            stock1.setStatusOfTheStock("LISTED");
            stock1.setStockBuyDate(LocalDateTime.now());
            stock1.setUser(user1);

            StockDetail stock2 = new StockDetail();
            stock2.setStockName("Tesla Inc.");
            stock2.setStockPrice(700.0);
            stock2.setStatusOfTheStock("LISTED");
            stock2.setStockBuyDate(LocalDateTime.now());
            stock2.setUser(user2);

            stockRepo.saveAll(Arrays.asList(stock1, stock2));

            System.out.println("Bootstrap stock data loaded successfully!");
        }
    }
}

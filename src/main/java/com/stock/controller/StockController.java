package com.stock.controller;

import com.stock.dto.*;
import com.stock.service.AuthService;
import com.stock.service.StockService;
import com.stock.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/stock")
public class StockController {
    private final StockService stockService;
    private final AuthService authService;
    private final WalletService walletService;
    public StockController(StockService s, AuthService a, WalletService w){
        this.stockService = s; this.authService = a; this.walletService = w;
    }

    @GetMapping("/get_all_stock")
    public ResponseEntity<List<?>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @PostMapping("/create_account")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @GetMapping("/load_amount")
    public ResponseEntity<?> loadAmount(@RequestParam Double amount, Principal principal){
        // call third-party and credit to wallet
        walletService.loadAmount(principal.getName(), amount);
        return ResponseEntity.ok("Load request submitted");
    }

    @PostMapping("/order")
    public ResponseEntity<?> order(@RequestBody OrderRequest order, Principal principal){
        stockService.createOrder(order, principal.getName());
        return ResponseEntity.ok("Order created");
    }

    @PostMapping("/store/stockList")
    public ResponseEntity<?> storeStocks(@RequestBody List<StockDTO> list){
        stockService.storeStockList(list);
        return ResponseEntity.ok("Stored");
    }

    @GetMapping("/status")
    public ResponseEntity<?> getOrderStatus(@RequestParam Long orderId) {
        return ResponseEntity.ok(stockService.getStatus(orderId));
    }

    @PostMapping("/update_amount")
    public ResponseEntity<?> updateAmount(@RequestBody UpdateAmountDto dto, Principal p){
        walletService.updateAmount(p.getName(), dto.getAmount());
        return ResponseEntity.ok("Updated");
    }
}


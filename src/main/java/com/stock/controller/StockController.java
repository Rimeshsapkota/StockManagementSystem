package com.stock.controller;

import com.stock.dto.*;
import com.stock.entity.VerifyKittaRequest;
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
    /**
     * Get all available stocks.
     *
     * @return ResponseEntity containing a list of stocks
     */
    @GetMapping("/get_all_stock")
    public ResponseEntity<List<?>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    /**
     * Register a new user account.
     *
     * @param req RegisterRequest DTO containing user registration details
     * @return ResponseEntity with registration response
     */
    @PostMapping("/create_account")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    /**
     * Authenticate a user and provide a JWT token.
     *
     * @param req AuthRequest DTO containing username and password
     * @return ResponseEntity with AuthResponse containing JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    /**
     * Load amount into the user's wallet.
     *
     * @param amount Amount to be loaded
     * @param principal Principal object containing authenticated user's information
     * @return ResponseEntity with confirmation message
     */
    @GetMapping("/load_amount")
    public ResponseEntity<?> loadAmount(@RequestParam Double amount, Principal principal){
        // call third-party and credit to wallet
        walletService.loadAmount(principal.getName(), amount);
        return ResponseEntity.ok("Load request submitted");
    }
    /**
     * Create a stock order for the authenticated user.
     *
     * @param order OrderRequest DTO containing order details
     * @param principal Principal object containing authenticated user's information
     * @return ResponseEntity with confirmation message
     */
    @PostMapping("/order")
    public ResponseEntity<?> order(@RequestBody OrderRequest order, Principal principal){
        stockService.createOrder(order, principal.getName());
        return ResponseEntity.ok("Order created");
    }
    /**
     * Store a list of stocks in the system.
     *
     * @param list List of StockDTO objects
     * @return ResponseEntity with confirmation message
     */
    @PostMapping("/store/stockList")
    public ResponseEntity<?> storeStocks(@RequestBody List<StockDTO> list){
        stockService.storeStockList(list);
        return ResponseEntity.ok("Stored");
    }
    /**
     * Get the status of an order by order ID.
     *
     * @param orderId ID of the order
     * @return ResponseEntity with order status
     */
    @GetMapping("/status")
    public ResponseEntity<?> getOrderStatus(@RequestParam Long orderId) {
        return ResponseEntity.ok(stockService.getStatus(orderId));
    }
    /**
     * Update the user's wallet amount.
     *
     * @param dto UpdateAmountDto containing the new amount
     * @param p Principal object containing authenticated user's information
     * @return ResponseEntity with confirmation message
     */
    @PostMapping("/update_amount")
    public ResponseEntity<?> updateAmount(@RequestBody UpdateAmountDto dto, Principal p){
        walletService.updateAmount(p.getName(), dto.getAmount());
        return ResponseEntity.ok("Updated");
    }

    /**
     * Verify if the user has enough kitta (shares) for a stock before selling.
     *
     * @param request VerifyKittaRequest DTO containing stock name and number of kitta
     * @param principal Principal object containing authenticated user's information
     * @return ResponseEntity with success or error message
     */
    @PostMapping("/verify-kitta")
    public ResponseEntity<?> verifyKitta(@RequestBody VerifyKittaRequest request, Principal principal) {
        try {
            // Assuming principal.getName() returns the username
            Long userId = stockService.getUserIdByUsername(principal.getName());
            stockService.verifyUserKitta(userId, request.getStockName(), request.getNumberOfKitta());

            return ResponseEntity.ok("You have enough kitta to sell.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


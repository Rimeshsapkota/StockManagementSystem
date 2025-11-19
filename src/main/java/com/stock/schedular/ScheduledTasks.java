package com.stock.schedular;


import com.stock.service.AuthService;
import com.stock.service.StockService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {
    private final StockService stockService;
    private final AuthService authService;
    public ScheduledTasks(StockService s, AuthService a){ this.stockService=s; this.authService=a; }

    // runs every 1 minute (example). Adjust cron as needed.
    @Scheduled(fixedDelay = 60000)
    public void checkOrderStatuses(){
        stockService.checkPendingOrdersAndUpdateStatus();
    }

    // clear expired forgot-password codes every hour
    @Scheduled(cron = "0 0 * * * *")
    public void clearExpiredForgetCodes(){
        authService.clearExpiredForgetCodes();
    }
}


package com.stock.repository;

import com.stock.entity.StockDetail;
import com.stock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface StockDetailRepository extends JpaRepository<StockDetail, Long> {
    List<StockDetail> findByStatusOfTheStock(String pending);

    @Query("SELECT SUM(s.numberOfKitta) FROM StockDetail s " +
            "WHERE s.user.id = :userId AND s.stockName = :stockName AND s.statusOfTheStock = 'BUY'")
    Integer findTotalKittaByUserAndStockName(@Param("userId") Long userId,
                                             @Param("stockName") String stockName);
}

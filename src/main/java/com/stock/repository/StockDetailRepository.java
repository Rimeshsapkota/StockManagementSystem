package com.stock.repository;

import com.stock.entity.StockDetail;
import com.stock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StockDetailRepository extends JpaRepository<StockDetail, Long> {
    List<StockDetail> findByStatusOfTheStock(String pending);
}

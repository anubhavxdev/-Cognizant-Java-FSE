package com.cognizant.ormlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cognizant.ormlearn.model.Stock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    // Get stock details for a code between start and end dates
    List<Stock> findByCodeAndDateBetween(String code, Date start, Date end);

    // Get stock details for a code where close price is greater than a value
    List<Stock> findByCodeAndCloseGreaterThan(String code, BigDecimal closePrice);

    // Get top 3 stocks by transaction volume descending
    List<Stock> findTop3ByOrderByVolumeDesc();

    // Get top 3 stocks of a specific code by close price ascending (lowest close price)
    List<Stock> findTop3ByCodeOrderByCloseAsc(String code);
}

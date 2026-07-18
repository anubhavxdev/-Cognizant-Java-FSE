package com.cognizant.ormlearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cognizant.ormlearn.model.Stock;
import com.cognizant.ormlearn.repository.StockRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Transactional(readOnly = true)
    public List<Stock> getStocksByCodeAndDateBetween(String code, Date start, Date end) {
        return stockRepository.findByCodeAndDateBetween(code, start, end);
    }

    @Transactional(readOnly = true)
    public List<Stock> getGoogleStocksGreaterThan(BigDecimal closePrice) {
        return stockRepository.findByCodeAndCloseGreaterThan("GOOGL", closePrice);
    }

    @Transactional(readOnly = true)
    public List<Stock> getTop3StocksByVolume() {
        return stockRepository.findTop3ByOrderByVolumeDesc();
    }

    @Transactional(readOnly = true)
    public List<Stock> getLowestNetflixStocks() {
        return stockRepository.findTop3ByCodeOrderByCloseAsc("NFLX");
    }
}

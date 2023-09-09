package com.example.loatradelife.service;

import com.example.loatradelife.domain.MarketItemTradeInfoDaily;
import com.example.loatradelife.repository.MarketItemTradeInfoDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MarketItemTradeInfoDailyService {
    private final MarketItemTradeInfoDailyRepository marketItemTradeInfoDailyRepository;

    @Transactional
    public Long saveMarketItemTradeInfoDaily(MarketItemTradeInfoDaily marketItemTradeInfoDaily) {
        long id = -1;

        if (!marketItemTradeInfoDailyRepository.existsMarketItemTradeInfoDailyByMarketItemAndDate(
                marketItemTradeInfoDaily.getMarketItem(),
                marketItemTradeInfoDaily.getDate())
        ) {
            id = marketItemTradeInfoDailyRepository.save(marketItemTradeInfoDaily).getId();
        }

        return id;
    }

    public List<MarketItemTradeInfoDaily> findAllMarketItemTradeInfoDaily() {
        return marketItemTradeInfoDailyRepository.findAll();
    }

    public Optional<MarketItemTradeInfoDaily> findOneMarketItemTradeInfoDaily(long id) {
        return marketItemTradeInfoDailyRepository.findById(id);
    }

    public List<MarketItemTradeInfoDaily> findListByDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return marketItemTradeInfoDailyRepository.findByDateBetweenOrderByDate(startDate, endDate);
    }

    @Transactional
    public void updateMarketItemTradeInfoDaily(long id, MarketItemTradeInfoDaily marketItemTradeInfoDaily) {
        Optional<MarketItemTradeInfoDaily> findById = marketItemTradeInfoDailyRepository.findById(id);
        findById.ifPresent(value -> value.updateMarketItemTradeInfoDaily(marketItemTradeInfoDaily));
    }

    @Transactional
    public void deleteMarketItemTradeInfoDaily(long id) {
        marketItemTradeInfoDailyRepository.deleteById(id);
    }
}


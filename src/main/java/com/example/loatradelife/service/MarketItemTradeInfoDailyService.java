package com.example.loatradelife.service;

import com.example.loatradelife.domain.MarketItem;
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
        long id;

        Optional<MarketItemTradeInfoDaily> findOne = marketItemTradeInfoDailyRepository.findByMarketItemAndDate(
                marketItemTradeInfoDaily.getMarketItem(),
                marketItemTradeInfoDaily.getDate()
        );
        if (findOne.isPresent()) {
            MarketItemTradeInfoDaily existOne = findOne.get();
            existOne.updateTradeCount(marketItemTradeInfoDaily.getTradeCount());
            id = existOne.getId();
        } else {
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

    public List<MarketItemTradeInfoDaily> findListByMarketItemAndDateBetween(MarketItem marketItem, LocalDateTime startDate, LocalDateTime endDate) {
        return marketItemTradeInfoDailyRepository.findByMarketItemAndDateBetweenOrderByDate(marketItem, startDate, endDate);
    }

    @Transactional
    public void updateMarketItemTradeInfoDaily(long id, MarketItemTradeInfoDaily marketItemTradeInfoDaily) {
        Optional<MarketItemTradeInfoDaily> findById = marketItemTradeInfoDailyRepository.findById(id);
        findById.ifPresent(value -> value.update(marketItemTradeInfoDaily));
    }

    @Transactional
    public void deleteMarketItemTradeInfoDaily(long id) {
        marketItemTradeInfoDailyRepository.deleteById(id);
    }
}


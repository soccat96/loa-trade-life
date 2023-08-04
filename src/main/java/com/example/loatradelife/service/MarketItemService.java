package com.example.loatradelife.service;

import com.example.loatradelife.domain.MarketItem;
import com.example.loatradelife.repository.MarketItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketItemService {
    private final MarketItemRepository marketItemRepository;

    @Transactional
    public Long saveMarketItem(MarketItem marketItem) {
        return marketItemRepository.save(marketItem).getId();
    }

    public List<MarketItem> findAllMarketItem() {
        return marketItemRepository.findAll();
    }

    public Optional<MarketItem> findOneMarketItem(long id) {
        return marketItemRepository.findById(id);
    }

    @Transactional
    public void updateMarketItem(long id, MarketItem marketItem) {
        Optional<MarketItem> market = marketItemRepository.findById(id);
        market.ifPresent(value -> value.updateMarketItem(marketItem));
    }

    @Transactional
    public void changeMarketItemUseAt(long id, boolean useAt) {
        Optional<MarketItem> marketItem = marketItemRepository.findById(id);
        marketItem.ifPresent(value -> value.changeUseAt(useAt));
    }
}
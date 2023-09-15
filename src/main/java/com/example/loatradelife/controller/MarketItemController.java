package com.example.loatradelife.controller;

import com.example.loatradelife.controller.dto.MarketItemDto;
import com.example.loatradelife.domain.MarketItem;
import com.example.loatradelife.domain.MarketItemTradeInfoDaily;
import com.example.loatradelife.service.MarketItemService;
import com.example.loatradelife.service.MarketItemTradeInfoDailyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/market-items")
@RequiredArgsConstructor
public class MarketItemController {
    private final MarketItemService marketItemService;
    private final MarketItemTradeInfoDailyService marketItemTradeInfoDailyService;

    @GetMapping
    public Result<List<MarketItemDto>> getMarketItems(@RequestParam(required = false, name = "categoryCode") String categoryCode) {
        ArrayList<MarketItemDto> marketItemDtoList = new ArrayList<>();
        List<MarketItem> marketItemList;
        if (!categoryCode.isEmpty()) {
            marketItemList = marketItemService.findListMarketItemByCategoryCode(Integer.parseInt(categoryCode));
        } else {
            marketItemList = marketItemService.findAllMarketItem();
        }
        for (MarketItem x : marketItemList) {
            marketItemDtoList.add(new MarketItemDto(x));
        }

        return new Result<>(marketItemDtoList);
    }

    @GetMapping("/{id}")
    public Result<MarketItemDto> getMarketItem(@PathVariable(name = "id") String id) {
        Optional<MarketItem> marketItem = marketItemService.findOneMarketItem(Long.parseLong(id));
        return marketItem.map(item -> new Result<>(new MarketItemDto(item))).orElseGet(() -> new Result<>(new MarketItemDto()));
    }

    @GetMapping("/{id}/data")
    public Result<MarketItemDto> getMarketItemWithData(
            @PathVariable(name = "id") String id,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        Optional<MarketItem> marketItem = marketItemService.findOneMarketItem(Long.parseLong(id));
        if (marketItem.isPresent()) {
            MarketItem mi = marketItem.get();
            Map<String, LocalDateTime> mapSdEd = StartDateEndDateMaker.getStartDateEndDate(startDate, endDate);
            List<MarketItemTradeInfoDaily> dataList = marketItemTradeInfoDailyService.findListByMarketItemAndDateBetween(mi, mapSdEd.get("sd"), mapSdEd.get("ed"));

            return new Result<>(new MarketItemDto(mi, dataList));
        } else {
            return new Result<>(new MarketItemDto());
        }
    }
}

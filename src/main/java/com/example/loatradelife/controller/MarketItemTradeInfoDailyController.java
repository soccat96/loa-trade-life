package com.example.loatradelife.controller;

import com.example.loatradelife.domain.ItemGrade;
import com.example.loatradelife.domain.MarketItem;
import com.example.loatradelife.domain.MarketItemTradeInfoDaily;
import com.example.loatradelife.service.MarketItemTradeInfoDailyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/market-item-trade-info-daily")
@RequiredArgsConstructor
public class MarketItemTradeInfoDailyController {
    private final MarketItemTradeInfoDailyService marketItemTradeInfoDailyService;

    @GetMapping
    public Result<List<MarketItemTradeInfoDailyDto>> getMarketItemTradeInfoDailies(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        Map<String, LocalDateTime> mapSdEd = StartDateEndDateMaker.getStartDateEndDate(startDate, endDate);

        List<MarketItemTradeInfoDailyDto> dtoList = new ArrayList<>();
        List<MarketItemTradeInfoDaily> dataList = marketItemTradeInfoDailyService.findListByDateBetween(mapSdEd.get("sd"), mapSdEd.get("ed"));
        for (MarketItemTradeInfoDaily x : dataList) {
            dtoList.add(new MarketItemTradeInfoDailyDto(x));
        }

        return new Result<>(dtoList);
    }

    @GetMapping("/{id}")
    public Result<MarketItemTradeInfoDailyDto> getMarketItemTradeInfoDaily(@PathVariable(name = "id") String id) {
        Optional<MarketItemTradeInfoDaily> data = marketItemTradeInfoDailyService.findOneMarketItemTradeInfoDaily(Long.parseLong(id));
        return data.map(marketItemTradeInfoDaily -> new Result<>(new MarketItemTradeInfoDailyDto(marketItemTradeInfoDaily))).orElseGet(() -> new Result<>(new MarketItemTradeInfoDailyDto()));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class MarketItemTradeInfoDailyDto {
        private Integer number;
        private String itemGradeName;

        private Integer categoryCode;
        private Integer code;
        private String marketItemName;
        private Integer tier;
        private String imageLink;

        private Long id;
        private LocalDateTime date;
        private Double avgPrice;
        private Integer tradeCount;
        private Integer bundleCount;

        public MarketItemTradeInfoDailyDto(MarketItemTradeInfoDaily marketItemTradeInfoDaily) {
            MarketItem marketItem = marketItemTradeInfoDaily.getMarketItem();
            ItemGrade itemGrade = marketItem.getItemGrade();

            this.number = itemGrade.getNumber();
            this.itemGradeName = itemGrade.getName();

            this.categoryCode = marketItem.getCategoryCode();
            this.code = marketItem.getCode();
            this.marketItemName = marketItem.getName();
            this.tier = marketItem.getTier();
            this.imageLink = marketItem.getImageLink();

            this.id = marketItemTradeInfoDaily.getId();
            this.date = marketItemTradeInfoDaily.getDate();
            this.avgPrice = marketItemTradeInfoDaily.getAvgPrice();
            this.tradeCount = marketItemTradeInfoDaily.getTradeCount();
            this.bundleCount = marketItemTradeInfoDaily.getBundleCount();
        }
    }
}

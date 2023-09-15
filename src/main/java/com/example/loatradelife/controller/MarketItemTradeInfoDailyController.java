package com.example.loatradelife.controller;

import com.example.loatradelife.controller.dto.MarketItemTradeInfoDailyDto;
import com.example.loatradelife.domain.MarketItemTradeInfoDaily;
import com.example.loatradelife.service.MarketItemTradeInfoDailyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/market-item-trade-info-dailies")
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
}

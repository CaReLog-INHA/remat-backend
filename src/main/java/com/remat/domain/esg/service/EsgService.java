package com.remat.domain.esg.service;

import com.remat.domain.esg.converter.EsgConverter;
import com.remat.domain.esg.dto.EsgReqDTO;
import com.remat.domain.esg.dto.EsgResDTO;
import com.remat.domain.esg.entity.ESGReport;
import com.remat.domain.esg.entity.ESGReportDetail;
import com.remat.domain.esg.entity.ESGReportMonthly;
import com.remat.domain.esg.repository.ESGReportRepository;
import com.remat.domain.material.repository.MaterialRepository;
import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.entity.Trade;
import com.remat.domain.trade.entity.TradeRequest;
import com.remat.domain.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EsgService {

    private static final Double TREE_CARBON_REDUCTION_KG = 6.8;

    private final TradeRepository tradeRepository;
    private final MaterialRepository materialRepository;
    private final ESGReportRepository esgReportRepository;

    @Transactional
    public EsgResDTO.EsgReportDTO getEsgReports(Member member, EsgReqDTO.EsgReportDateDTO reqDto) {
        List<Trade> trades = tradeRepository.findCompletedTradesByMemberIdAndPeriod(
                member.getId(),
                reqDto.periodStart().atStartOfDay(),
                reqDto.periodEnd().plusDays(1).atStartOfDay()
        );

        List<ESGReportDetail> esgReportDetails = toReportDetails(trades);
        List<ESGReportMonthly> esgReportMonthlies = aggregateMonthlyReports(
                trades,
                reqDto.periodStart(),
                reqDto.periodEnd()
        );

        Double totalCarbonReductionKg = esgReportDetails.stream()
                .mapToDouble(ESGReportDetail::getCarbonKg)
                .sum();

        Integer resourceReuseRate = calculateResourceReuseRate(
                member, trades, reqDto.periodStart(), reqDto.periodEnd());
        Integer treeCount = (int) Math.round(totalCarbonReductionKg / TREE_CARBON_REDUCTION_KG);

        ESGReport esgReport = EsgConverter.toEsgReport(
                member,
                totalCarbonReductionKg,
                resourceReuseRate,
                treeCount,
                trades.size(),
                reqDto.periodStart(),
                reqDto.periodEnd()
        );

        esgReportDetails.stream().forEach(esgReport::addReportDetails);
        esgReportMonthlies.stream().forEach(esgReport::addMonthlyReport);

        esgReportRepository.save(esgReport);
        return EsgConverter.toEsgReportDTO(esgReport);
    }

    private Integer calculateResourceReuseRate(
            Member member,
            List<Trade> trades,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {
        LocalDateTime start = periodStart.atStartOfDay();
        LocalDateTime end = periodEnd.plusDays(1).atStartOfDay();

        long listedCount = materialRepository.countListedMaterialsInPeriod(member.getId(), start, end);
        if (listedCount == 0) {
            return 0;
        }

        long reusedCount = trades.stream()
                .filter(trade -> trade.getSeller().getId().equals(member.getId()))
                .map(trade -> trade.getTradeRequest().getRequestMaterial())
                .filter(material -> !material.getCreatedAt().isBefore(start)
                        && material.getCreatedAt().isBefore(end))
                .map(material -> material.getId())
                .distinct()
                .count();

        return (int) Math.round(reusedCount * 100.0 / listedCount);
    }

    private List<ESGReportDetail> toReportDetails(List<Trade> trades) {
        return trades.stream()
                .map(trade -> EsgConverter.toEsgReportDetail(trade, calculateCarbonReductionKg(trade)))
                .toList();
    }

    private List<ESGReportMonthly> aggregateMonthlyReports(
            List<Trade> trades,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {
        Map<YearMonth, Double> carbonByMonth = trades.stream()
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getCreatedAt()),
                        Collectors.summingDouble(this::calculateCarbonReductionKg)
                ));

        List<ESGReportMonthly> result = new ArrayList<>();
        YearMonth end = YearMonth.from(periodEnd);
        for (YearMonth ym = YearMonth.from(periodStart); !ym.isAfter(end); ym = ym.plusMonths(1)) {
            result.add(ESGReportMonthly.builder()
                    .year(ym.getYear())
                    .month(ym.getMonthValue())
                    .carbonKg(carbonByMonth.getOrDefault(ym, 0.0))
                    .build()
            );
        }
        return result;
    }

    private Double calculateCarbonReductionKg(Trade trade) {
        TradeRequest tradeRequest = trade.getTradeRequest();
        return tradeRequest.getQuantity()
                * tradeRequest.getRequestMaterial().getCategory().calculateAvgCarbonReductionKg();
    }
}

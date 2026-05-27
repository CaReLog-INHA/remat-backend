package com.remat.domain.member.service;

import com.remat.domain.esg.entity.ESGReport;
import com.remat.domain.esg.repository.ESGReportRepository;
import com.remat.domain.material.entity.Material;
import com.remat.domain.material.repository.MaterialRepository;
import com.remat.domain.member.converter.MyPageConverter;
import com.remat.domain.member.dto.MyPageResDTO;
import com.remat.domain.member.entity.Member;
import com.remat.domain.member.entity.mapping.MemberBadge;
import com.remat.domain.member.repository.MemberBadgeRepository;
import com.remat.domain.trade.entity.Trade;
import com.remat.domain.trade.repository.TradeRepository;
import com.remat.domain.trade.repository.TradeReviewRepository;
import com.remat.global.service.R2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private static final int RECENT_ACTIVITY_LIMIT = 5;

    private final ESGReportRepository esgReportRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final MaterialRepository materialRepository;
    private final TradeRepository tradeRepository;
    private final TradeReviewRepository tradeReviewRepository;
    private final R2Service r2Service;

    public MyPageResDTO.OverviewDTO getMyPage(Member member) {
        return new MyPageResDTO.OverviewDTO(
                getProfile(member),
                getESGContribution(member),
                getBadges(member),
                getSellingMaterials(member),
                getRecentActivities(member)
        );
    }

    public MyPageResDTO.ProfileDTO getProfile(Member member) {
        return MyPageConverter.toProfileDTO(member);
    }

    public MyPageResDTO.ESGContributionDTO getESGContribution(Member member) {
        ESGReport esgReport = esgReportRepository
                .findTopByMemberIdAndDeletedAtIsNullOrderByPeriodEndDescCreatedAtDesc(member.getId())
                .orElse(null);

        return MyPageConverter.toESGContributionDTO(esgReport);
    }

    public MyPageResDTO.ESGReportDTO getESGReport(Member member) {
        ESGReport esgReport = esgReportRepository
                .findTopByMemberIdAndDeletedAtIsNullOrderByPeriodEndDescCreatedAtDesc(member.getId())
                .orElse(null);

        if (esgReport == null) {
            return MyPageConverter.toESGReportDTO(member, null, List.of(), List.of());
        }

        List<MyPageResDTO.ESGMonthlyDTO> monthlyReports = esgReport.getMonthlyReports()
                .stream()
                .sorted(Comparator
                        .comparing(com.remat.domain.esg.entity.ESGReportMonthly::getYear)
                        .thenComparing(com.remat.domain.esg.entity.ESGReportMonthly::getMonth))
                .map(MyPageConverter::toESGMonthlyDTO)
                .toList();

        List<MyPageResDTO.ESGReportDetailDTO> reportDetails = esgReport.getReportDetails()
                .stream()
                .sorted(Comparator.comparing(
                        com.remat.domain.esg.entity.ESGReportDetail::getTradeDate,
                        Comparator.reverseOrder()
                ))
                .map(MyPageConverter::toESGReportDetailDTO)
                .toList();

        return MyPageConverter.toESGReportDTO(member, esgReport, monthlyReports, reportDetails);
    }

    public MyPageResDTO.BadgeSectionDTO getBadges(Member member) {
        List<MemberBadge> memberBadges = memberBadgeRepository.findAllByMemberIdWithBadge(member.getId());
        int inProgressRate = calculateInProgressBadgeRate(member.getId());

        List<MyPageResDTO.BadgeDTO> earnedBadges = memberBadges.stream()
                .filter(memberBadge -> Boolean.TRUE.equals(memberBadge.getIsEarned()))
                .map(memberBadge -> MyPageConverter.toBadgeDTO(memberBadge, 100))
                .toList();

        List<MyPageResDTO.BadgeDTO> inProgressBadges = memberBadges.stream()
                .filter(memberBadge -> !Boolean.TRUE.equals(memberBadge.getIsEarned()))
                .map(memberBadge -> MyPageConverter.toBadgeDTO(memberBadge, inProgressRate))
                .toList();

        return new MyPageResDTO.BadgeSectionDTO(earnedBadges, inProgressBadges);
    }

    public List<MyPageResDTO.SellingMaterialDTO> getSellingMaterials(Member member) {
        return materialRepository.findByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(member.getId())
                .stream()
                .map(material -> MyPageConverter.toSellingMaterialDTO(
                        material,
                        r2Service.getFileUrl(material.getImageKey())
                ))
                .toList();
    }

    public MyPageResDTO.RecentActivitiesDTO getRecentActivities(Member member) {
        Long memberId = member.getId();
        PageRequest limit = PageRequest.of(0, RECENT_ACTIVITY_LIMIT);

        List<MyPageResDTO.TradeActivityDTO> completedPurchases = tradeRepository
                .findCompletedPurchasesByMemberId(memberId, limit)
                .stream()
                .map(trade -> MyPageConverter.toPurchaseActivityDTO(trade, getMaterialImageUrl(trade)))
                .toList();

        List<MyPageResDTO.TradeActivityDTO> completedSales = tradeRepository
                .findCompletedSalesByMemberId(memberId, limit)
                .stream()
                .map(trade -> MyPageConverter.toSaleActivityDTO(trade, getMaterialImageUrl(trade)))
                .toList();

        List<MyPageResDTO.ReviewActivityDTO> receivedReviews = tradeReviewRepository
                .findReceivedReviewsByMemberId(memberId, limit)
                .stream()
                .map(MyPageConverter::toReceivedReviewActivityDTO)
                .toList();

        List<MyPageResDTO.ReviewActivityDTO> writtenReviews = tradeReviewRepository
                .findWrittenReviewsByMemberId(memberId, limit)
                .stream()
                .map(MyPageConverter::toWrittenReviewActivityDTO)
                .toList();

        List<MyPageResDTO.BadgeActivityDTO> earnedBadgeHistories = memberBadgeRepository
                .findEarnedByMemberIdWithBadge(memberId)
                .stream()
                .limit(RECENT_ACTIVITY_LIMIT)
                .map(MyPageConverter::toBadgeActivityDTO)
                .toList();

        return new MyPageResDTO.RecentActivitiesDTO(
                completedPurchases,
                completedSales,
                receivedReviews,
                writtenReviews,
                earnedBadgeHistories
        );
    }

    private String getMaterialImageUrl(Trade trade) {
        Material material = trade.getTradeRequest().getRequestMaterial();
        return r2Service.getFileUrl(material.getImageKey());
    }

    private int calculateInProgressBadgeRate(Long memberId) {
        long completedTradeCount = tradeRepository.countByBuyerIdAndDeletedAtIsNull(memberId)
                + tradeRepository.countBySellerIdAndDeletedAtIsNull(memberId);
        long reviewCount = tradeReviewRepository.countByReviewerId(memberId)
                + tradeReviewRepository.countByRevieweeId(memberId);

        return Math.min(99, (int) (completedTradeCount * 20 + reviewCount * 10));
    }
}

package com.remat.domain.member.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MyPageResDTO {

    public record OverviewDTO(
            ProfileDTO profile,
            ESGContributionDTO esgContribution,
            BadgeSectionDTO badges,
            List<SellingMaterialDTO> sellingMaterials,
            RecentActivitiesDTO recentActivities
    ) {}

    public record ProfileDTO(
            Long memberId,
            String name,
            String email,
            String phoneNumber,
            String companyName,
            String region,
            Integer starRating
    ) {}

    public record ESGContributionDTO(
            Integer totalCarbonKg,
            Integer totalTradeCount,
            Integer resourceReuseRate,
            Integer treeCount
    ) {}

    public record ESGReportDTO(
            Long reportId,
            String companyName,
            LocalDate periodStart,
            LocalDate periodEnd,
            LocalDateTime generatedAt,
            ESGContributionDTO summary,
            List<ESGMonthlyDTO> monthlyReports,
            List<ESGReportDetailDTO> reportDetails
    ) {}

    public record ESGMonthlyDTO(
            Integer year,
            Integer month,
            Integer carbonKg
    ) {}

    public record ESGReportDetailDTO(
            Long tradeId,
            LocalDate tradeDate,
            String materialName,
            Integer quantity,
            Integer carbonKg
    ) {}

    public record BadgeSectionDTO(
            List<BadgeDTO> earnedBadges,
            List<BadgeDTO> inProgressBadges
    ) {}

    public record BadgeDTO(
            Long memberBadgeId,
            Long badgeId,
            String badgeName,
            String description,
            String iconUrl,
            Boolean isEarned,
            Integer progressRate,
            LocalDateTime earnedAt
    ) {}

    public record SellingMaterialDTO(
            Long id,
            String imageUrl,
            String materialName,
            Boolean isSelling,
            String description,
            Integer price
    ) {}

    public record RecentActivitiesDTO(
            List<TradeActivityDTO> completedPurchases,
            List<TradeActivityDTO> completedSales,
            List<ReviewActivityDTO> receivedReviews,
            List<ReviewActivityDTO> writtenReviews,
            List<BadgeActivityDTO> earnedBadgeHistories
    ) {}

    public record TradeActivityDTO(
            Long tradeId,
            Long materialId,
            String materialName,
            String imageUrl,
            String partnerName,
            Integer quantity,
            Integer finalPrice,
            LocalDateTime createdAt
    ) {}

    public record ReviewActivityDTO(
            Long reviewId,
            Long tradeId,
            String partnerName,
            Integer starRating,
            String description,
            LocalDateTime createdAt
    ) {}

    public record BadgeActivityDTO(
            Long memberBadgeId,
            Long badgeId,
            String badgeName,
            String iconUrl,
            LocalDateTime earnedAt
    ) {}
}

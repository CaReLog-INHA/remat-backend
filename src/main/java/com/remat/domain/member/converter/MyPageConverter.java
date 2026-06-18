package com.remat.domain.member.converter;

import com.remat.domain.esg.entity.ESGReport;
import com.remat.domain.esg.entity.ESGReportDetail;
import com.remat.domain.esg.entity.ESGReportMonthly;
import com.remat.domain.material.entity.Material;
import com.remat.domain.member.dto.MyPageResDTO;
import com.remat.domain.member.entity.Member;
import com.remat.domain.member.entity.mapping.MemberBadge;
import com.remat.domain.trade.entity.Trade;
import com.remat.domain.trade.entity.TradeReview;

public class MyPageConverter {

    public static MyPageResDTO.ProfileDTO toProfileDTO(Member member) {
        return new MyPageResDTO.ProfileDTO(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getCompanyName(),
                member.getRegion().getKoreanName(),
                member.getStarRating()
        );
    }

    public static MyPageResDTO.ESGContributionDTO toESGContributionDTO(ESGReport esgReport) {
        if (esgReport == null) {
            return new MyPageResDTO.ESGContributionDTO(0.0, 0, 0, 0);
        }

        return new MyPageResDTO.ESGContributionDTO(
                esgReport.getTotalCarbonKg(),
                esgReport.getTotalTradeCount(),
                esgReport.getResourceReuseRate(),
                esgReport.getTreeCount()
        );
    }

    public static MyPageResDTO.ESGReportDTO toESGReportDTO(
            Member member,
            ESGReport esgReport,
            java.util.List<MyPageResDTO.ESGMonthlyDTO> monthlyReports,
            java.util.List<MyPageResDTO.ESGReportDetailDTO> reportDetails
    ) {
        if (esgReport == null) {
            return new MyPageResDTO.ESGReportDTO(
                    null,
                    member.getCompanyName(),
                    null,
                    null,
                    null,
                    toESGContributionDTO(null),
                    monthlyReports,
                    reportDetails
            );
        }

        return new MyPageResDTO.ESGReportDTO(
                esgReport.getId(),
                member.getCompanyName(),
                esgReport.getPeriodStart(),
                esgReport.getPeriodEnd(),
                esgReport.getCreatedAt(),
                toESGContributionDTO(esgReport),
                monthlyReports,
                reportDetails
        );
    }

    public static MyPageResDTO.ESGMonthlyDTO toESGMonthlyDTO(ESGReportMonthly monthlyReport) {
        return new MyPageResDTO.ESGMonthlyDTO(
                monthlyReport.getYear(),
                monthlyReport.getMonth(),
                monthlyReport.getCarbonKg()
        );
    }

    public static MyPageResDTO.ESGReportDetailDTO toESGReportDetailDTO(ESGReportDetail reportDetail) {
        return new MyPageResDTO.ESGReportDetailDTO(
                reportDetail.getTrade().getId(),
                reportDetail.getTradeDate(),
                reportDetail.getMaterialName(),
                reportDetail.getQuantity(),
                reportDetail.getCarbonKg()
        );
    }

    public static MyPageResDTO.BadgeDTO toBadgeDTO(MemberBadge memberBadge, Integer progressRate) {
        return new MyPageResDTO.BadgeDTO(
                memberBadge.getId(),
                memberBadge.getBadge().getId(),
                memberBadge.getBadge().getBadgeName(),
                memberBadge.getBadge().getDescription(),
                memberBadge.getBadge().getIconUrl(),
                memberBadge.getIsEarned(),
                progressRate,
                Boolean.TRUE.equals(memberBadge.getIsEarned()) ? memberBadge.getCreatedAt() : null
        );
    }

    public static MyPageResDTO.SellingMaterialDTO toSellingMaterialDTO(Material material, String imageUrl) {
        return new MyPageResDTO.SellingMaterialDTO(
                material.getId(),
                imageUrl,
                material.getMaterialName(),
                material.getDeletedAt() == null,
                material.getDescription(),
                material.getPrice()
        );
    }

    public static MyPageResDTO.TradeActivityDTO toPurchaseActivityDTO(Trade trade, String imageUrl) {
        Material material = trade.getTradeRequest().getRequestMaterial();
        return new MyPageResDTO.TradeActivityDTO(
                trade.getId(),
                material.getId(),
                material.getMaterialName(),
                imageUrl,
                trade.getSeller().getName(),
                trade.getTradeRequest().getQuantity(),
                trade.getFinalPrice(),
                trade.getCreatedAt()
        );
    }

    public static MyPageResDTO.TradeActivityDTO toSaleActivityDTO(Trade trade, String imageUrl) {
        Material material = trade.getTradeRequest().getRequestMaterial();
        return new MyPageResDTO.TradeActivityDTO(
                trade.getId(),
                material.getId(),
                material.getMaterialName(),
                imageUrl,
                trade.getBuyer().getName(),
                trade.getTradeRequest().getQuantity(),
                trade.getFinalPrice(),
                trade.getCreatedAt()
        );
    }

    public static MyPageResDTO.ReviewActivityDTO toReceivedReviewActivityDTO(TradeReview review) {
        return new MyPageResDTO.ReviewActivityDTO(
                review.getId(),
                review.getTrade().getId(),
                review.getReviewer().getName(),
                review.getStarRating(),
                review.getDescription(),
                review.getCreatedAt()
        );
    }

    public static MyPageResDTO.ReviewActivityDTO toWrittenReviewActivityDTO(TradeReview review) {
        return new MyPageResDTO.ReviewActivityDTO(
                review.getId(),
                review.getTrade().getId(),
                review.getReviewee().getName(),
                review.getStarRating(),
                review.getDescription(),
                review.getCreatedAt()
        );
    }

    public static MyPageResDTO.BadgeActivityDTO toBadgeActivityDTO(MemberBadge memberBadge) {
        return new MyPageResDTO.BadgeActivityDTO(
                memberBadge.getId(),
                memberBadge.getBadge().getId(),
                memberBadge.getBadge().getBadgeName(),
                memberBadge.getBadge().getIconUrl(),
                memberBadge.getCreatedAt()
        );
    }
}

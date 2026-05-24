package com.povod9.adcampaign.dto;

import com.povod9.adcampaign.entity.ProductEntity;
import com.povod9.adcampaign.enums.TownName;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record CampaignUpdateRequest(
        String name,
        List<String> keywords,
        @Positive BigDecimal bidAmount,
        @Positive BigDecimal campaignFund,
        List<TownName> town,
        @Positive Integer radius,
        Long productId
) {
}

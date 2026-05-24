package com.povod9.adcampaign.dto;

import com.povod9.adcampaign.entity.ProductEntity;
import com.povod9.adcampaign.enums.TownName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record CampaignRequest(
        @NotBlank String name,
        @NotEmpty List<String> keywords,
        @NotNull @Positive BigDecimal bidAmount,
        @NotNull @Positive BigDecimal campaignFund,
        @NotNull List<TownName> town,
        @NotNull @Positive Integer radius,
        @NotNull Long productId
) {
}

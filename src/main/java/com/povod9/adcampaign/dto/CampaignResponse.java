package com.povod9.adcampaign.dto;

import com.povod9.adcampaign.enums.Status;
import com.povod9.adcampaign.enums.TownName;

import java.math.BigDecimal;
import java.util.List;

public record CampaignResponse(
        Long campaignId,
        String name,
        List<String> keywords,
        BigDecimal bidAmount,
        BigDecimal campaignFund,
        Status status,
        List<TownName> town,
        Integer radius,
        Long productId
) {

}

package com.povod9.adcampaign.dto;

import com.povod9.adcampaign.enums.Status;
import com.povod9.adcampaign.enums.TownName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public record CampaignUpdateRequest(
    String name,
    List<String> keywords,
    @DecimalMin(value = "10.00", message = "Bid amount must be at least 10.00")
        BigDecimal bidAmount,
    @Positive(message = "Campaign fund must be greater than 0") BigDecimal campaignFund,
    Status status,
    List<TownName> town,
    @Positive Integer radius,
    Long productId) {}

package com.povod9.adcampaign.dto;

import jakarta.validation.constraints.NotNull;

public record SellerUpdateRequest(
        @NotNull String sellerName, @NotNull String email, @NotNull String password) {}

package com.povod9.adcampaign.dto;


public record ProductResponse(
        Long productId,
        String productName,
        SellerResponse seller
) {
}
